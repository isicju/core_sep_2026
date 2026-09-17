package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.datafaker.Faker;


public class GenerateRandomPersonListData {
    private Faker faker = new Faker();
   
    private final String[] PROFESSIONS = {"ARTIST", "IT", "ACCOUNTANT"};

    
    public List<Person> getRandomPersonList() {
        int quantityRandomPerson = faker.number().numberBetween(50, 100);

        List<Person>personList = new ArrayList<>(quantityRandomPerson);
        
        for(int i = 0; i < quantityRandomPerson; i++) {
            
            Person person = new Person(
                UUID.randomUUID(),
                faker.name().firstName(),
                faker.number().numberBetween(18, 55),
                faker.number().numberBetween(50_000, 150_000),
                faker.options().option(PROFESSIONS)
            );
           
            personList.add(person);
        }

        return personList;
    }
}
