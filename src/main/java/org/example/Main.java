package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) {
        
        if (args.length != 1) {
            log.error("File path isn't specified! Usage: java Main <file> <type>");
            System.exit(1);
        }
        
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        
        executor.scheduleAtFixedRate(() -> {
            GenerateRandomPersonListData generatePersonList = new GenerateRandomPersonListData();
            
            List<Person> personListData = generatePersonList.getRandomPersonList();
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
            String fileName = timestamp + "_data.txt";
            
            File file = new File(args[0], fileName);
            
            writeToJson(file, personListData);

        }, 0, 30, TimeUnit.SECONDS);
    }

    private static void writeToJson(File file, List<Person> personListData) {
         
        try {
            mapper.writeValue(file, personListData);
            log.info("data saved successfully");
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

}