pipeline {
    agent any

    stages {
        stage('Removing old containers') {
            steps {
                sh '''
                    docker rm -f hw5app || true
                '''
                sh '''
                    docker rm -f hw-5-postgres || true
                '''
                sh '''
                    docker rm -f hw-5-mortgage-minio || true
                '''
            }
        }

        stage('Starting postgres') {
            steps {
                sh '''
                    docker run -d \
                        --name hw-5-postgres \
                        -e POSTGRES_USER=mortgage \
                        -e POSTGRES_PASSWORD=mortgage \
                        -e POSTGRES_DB=mortgage \
                        -p 5439:5432 \
                        loandb:0.5
                '''
            }
        }

        stage('Starting minio') {
            steps {
                sh '''
                    docker run -d \
                        --name hw-5-mortgage-minio \
                        -e MINIO_ROOT_USER=minioadmin \
                        -e MINIO_ROOT_PASSWORD=minioadmin \
                        -p 9000:9000 \
                        -p 9001:9001 \
                        cgr.dev/chainguard/minio:latest \
                        server /data --console-address ":9001"
                '''
            }
        }
        
       stage('Starting new instance container hw5app') {
    steps {
        sh '''
            docker run -d \
                --name hw5app \
                -p 4343:8080 \
                -e MINIO_USER=minioadmin \
                -e MINIO_PASSWORD=minioadmin \
                -e MINIO_URL=http://YOUR_IP:9000 \
                -e POSTGRES_HOSTNAME=YOUR_IP \
                -e POSTGRES_PORT=5439 \
                -e POSTGRES_DATABASE=mortgage \
                -e POSTGRES_USER=mortgage \
                -e POSTGRES_PASSWORD=mortgage \
                hw5app:0.1 \
                sleep infinity
        '''
    }
       }
    stage('Pulling latest changes') {
            steps {
                sh '''
                    docker exec hw5app git -C /app pull
                '''
            }
        }

        stage('Building project without tests') {
            steps {
                sh '''
                    docker exec hw5app /app/gradlew clean build -x test
                '''
            }
        }

        stage('Running tests') {
            steps {
                sh '''
                    docker exec hw5app /app/gradlew test
                '''
            }
        }
        
        stage('Starting application') {
            steps {
                sh '''
                   docker exec -d hw5app java -jar /app/build/libs/lesson3-0.0.1-SNAPSHOT.jar
                '''
            }
        }
}
        
    }
