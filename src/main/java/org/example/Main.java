package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Command(name = "user-generator", mixinStandardHelpOptions = true, version = "1.0")
public class Main implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Option(names = {"--user_count", "--user-count"}, description = "Number of users to generate", defaultValue = "10")
    private int userCount;
    @Option(names = {"--sleep_time", "--sleep-time"}, description = "Sleep time between generations in milliseconds", defaultValue = "30000")
    private long sleepTime;

    @Override
    public void run() {
        UserGenerator userGenerator = FakerUserGenerator.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            try {
                File file = new File(System.currentTimeMillis() + "_data.txt");
                file.createNewFile();
                List<User> users = userGenerator.generateUsers(userCount);
                Files.writeString(Path.of(file.getAbsolutePath()), mapper.writeValueAsString(users));
                log.info("Generated {} users and saved to {}", userCount, file.getAbsolutePath());
                Thread.sleep(sleepTime);
            } catch (IOException e) {
                log.error("Failed to generate user data", e);
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("User generator interrupted");
                break;
            }
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}