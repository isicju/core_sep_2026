#!/usr/bin/env bash

set -uo pipefail

JAR_PATH="build/libs/gradle_based-1.0-SNAPSHOT.jar"
EMAIL_TO="put your email"
USER_FOLDER="/root/hw1/generator/core_sep_2026/users"
EMAIL_AUTH_TOKEN="put email auth token (from group)"
INTERVAL_SECONDS=30

LOG_FILE="run_every_30s.log"

run_once() {
    java -jar "$JAR_PATH" \
        --email_to "$EMAIL_TO" \
        --user_folder "$USER_FOLDER" \
        --email_auth_token "$EMAIL_AUTH_TOKEN"
}

echo "Starting loop: running $JAR_PATH every ${INTERVAL_SECONDS}s. Press Ctrl+C to stop."

while true; do
    timestamp="$(date '+%Y-%m-%d %H:%M:%S')"
    echo "[$timestamp] Running jar..."

    if [ -n "${LOG_FILE:-}" ]; then
        run_once >> "$LOG_FILE" 2>&1
    else
        run_once
    fi

    exit_code=$?
    if [ $exit_code -ne 0 ]; then
        echo "[$timestamp] Jar exited with code $exit_code"
    fi

    sleep "$INTERVAL_SECONDS"
done
