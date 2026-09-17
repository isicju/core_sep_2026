package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.example.model.UserAnalyticResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Command(name = "user-generator", mixinStandardHelpOptions = true, version = "1.0")
public class Main implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Option(names = {"--email_auth_token", "--email_auth_token"}, description = "Email auth token", required = true)
    private String emailNotificationToken;
    @Option(names = {"--sleep_time", "--sleep-time"}, description = "Sleep time between generations in milliseconds", defaultValue = "30000")
    private long sleepTime;
    @Option(names = {"--user_folder", "--user_folder"}, description = "Location of generated users", defaultValue = "users")
    private String userFolder;
    @Option(names = {"--email_to", "--email_to"}, description = "Email to be notified", required = true)
    private String emailTo;
    @Option(names = {"--analytics_folder", "--analytics_folder"}, description = "Location of previously analyzed users", defaultValue = "analytics.txt")
    private String analyticsFile;

    @Override
    public void run() {
        Path pathToFolder = Path.of(userFolder);
        UserReader userReader = new UserReader(new ObjectMapper());
        UserStorage userStorage = new UserStorage(Path.of(analyticsFile));
        EmailNotificationService emailNotificationService = new EmailNotificationService(emailNotificationToken);
        UserAnalyticsService userAnalyticResults = new UserAnalyticsService();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathToFolder)) {
            for (Path filePath : stream) {
                log.info("analyzing {}", filePath.getFileName());
                if (userStorage.fileWasAnalyzed(filePath)) {
                    log.info("file {} was analyzed", filePath.getFileName());
                    continue;
                }
                List<User> usersFromFile = userReader.readUserFromFile(filePath);
                List<UserAnalyticResults> analyticResults = userAnalyticResults.makeAnalysis(usersFromFile);

                emailNotificationService.sendEmail(emailTo, "new users been analyzed: " + analyticResults, analyticResults.toString().getBytes(UTF_8));
                userStorage.saveAsAnalyzed(filePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}