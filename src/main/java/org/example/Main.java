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
    @Option(names = {"--user_folder", "--user_folder"}, description = "Location of generated users", defaultValue = "users")
    private String userFolder;

    @Override
    public void run() {
        UserGenerator userGenerator = FakerUserGenerator.getInstance();
        UserStorage userStorage = new FileUserStorage(userFolder);

        while (true) {
            List<User> users = userGenerator.generateUsers(userCount);
            userStorage.persistUsers(users);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
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