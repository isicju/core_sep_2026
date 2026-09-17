package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main2 {

    private static final Logger log = LoggerFactory.getLogger(Main2.class);

    //{ "id": UUID, "name": string, "age" : int, "salary" : int, "PROFESSION": (ARTIST,IT,ACCOUNTANT) }

    public static void main(String[] args) throws IOException, InterruptedException {

        UserGenerator userGenerator = FakerUserGenerator.getInstance();

        ObjectMapper mapper = new ObjectMapper();

        while (true) {
            File file = new File(System.currentTimeMillis() + "_data.txt");
            file.createNewFile();

            int randomRecordNumber = ThreadLocalRandom.current().nextInt(10, 101);
            List<User> users = userGenerator.generateUsers(randomRecordNumber);

            Files.writeString(Path.of(file.getAbsolutePath()), mapper.writeValueAsString(users));
            Thread.sleep(30000);
        }
    }


    public static void main1(String[] args) {
        String content = """
                File content
                """;

        byte[] fileBytes = content.getBytes(UTF_8);

        EmailNotificationService emailNotificationService = new EmailNotificationService("TOKEN!");
        emailNotificationService.sendEmail("isicjua@gmail.com", "body text", fileBytes);
    }

}