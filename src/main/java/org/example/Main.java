package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {



        if (args.length != 2) {
            log.error("File path isn't specified! Usage: java Main <file> <type>");
            System.exit(1);
        }
        
        File directory = new File(args[0]);
        
        if (!directory.exists()) {
            log.error("Directory not exists");
            System.exit(1);
        }

        final Pattern DATA_FILE_PATTERN = Pattern.compile("\\d{2}-\\d{2}-\\d{4}_\\d{2}-\\d{2}-\\d{2}_data.txt");
        final String FILE_NAME = "listParsedGeneratedFiles.txt";
     
        File[] dataFiles = directory.listFiles((dir, name) -> !name.equals(FILE_NAME) && DATA_FILE_PATTERN.matcher(name).matches());        

        if (dataFiles == null) {
            log.error("No data files");
            return;
        }
        
        File registryFile = new File(directory, FILE_NAME);
        List<String> processedData = new ArrayList<>();

        Map<String, Object> registry = loadRegistry(registryFile);        

        for (int i = 0; i < dataFiles.length; i++) {
            String dataFileName = dataFiles[i].getName();

            if (registry.containsKey(dataFileName)) {
                continue;
            }

            try {
                Object parsedData = parseDataFile(dataFiles[i]);
                registry.put(dataFileName, parsedData);
                processedData.add(dataFileName);
            } catch (Exception e) {
                log.error("Failed parse");
            }
        }

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(registryFile, registry);

            log.info("Registry saved");
        } catch (IOException e) {
            log.error("Failed save registry");
            System.exit(1);
        }

        if (!processedData.isEmpty()) {
            try {                
                String body = """
                            Проанализированы файлы: %s
                            Время: %s
                            """.formatted(String.join(", ", processedData) ,LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-mm-yyyy")));;

                byte[] fileBytes = Files.readAllBytes(registryFile.toPath());
                
                EmailNotificationService emailNotificationService = new EmailNotificationService(args[1]);
                emailNotificationService.sendEmail("boss.dzhal@gmail.com", body, fileBytes);
                log.info("Registry was sent");
            } catch (IOException e) {
                log.error("Failed send email");
            }
        }
    }

    private static  Map<String, Object> loadRegistry(File registryFile) {

        if (!registryFile.exists()) {
            return new LinkedHashMap<>();
        }
        
        try {
            return mapper.readValue(
                registryFile,
                new TypeReference<LinkedHashMap<String, Object>>() {}
            );
        } catch (IOException e) {
            log.error("File not created");
            System.exit(1);
            return  new LinkedHashMap<>();
        }
            
    }

    private static List<ProfessionStatistics> parseDataFile(File dataFile) throws IOException {
        List<ProfessionStatistics> result = new ArrayList<>();
        List<Person> persons = mapper.readValue(dataFile, new TypeReference<List<Person>>() {});

        Map<String, List<Person>> byProfession = new LinkedHashMap<>();

        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);

            String profession = person.getProfession();
            List<Person> list = byProfession.get(profession);
            
            if (list == null) {
                list = new ArrayList<>();
                byProfession.put(profession, list);
            }

            list.add(person);
        }

        List<String> professions = new ArrayList<>(byProfession.keySet());

         for (int i = 0; i < professions.size(); i++) {
            String profession = professions.get(i);
            List<Person> list = byProfession.get(profession);

            long totalSalary = 0;
            long totalAge = 0;

            for (int k = 0; k < list.size(); k++) {
                totalSalary += list.get(k).getSalary();
                totalAge += list.get(k).getAge();
            }

            result.add(new ProfessionStatistics(
                profession,
                (int) totalSalary,
                (int) (totalAge / list.size())
            ));
         }
        

        return result;
    }
}