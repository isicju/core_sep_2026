package org.example;

import java.util.UUID;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    
    private UUID id;
    private String name;
    private int age;
    private int salary;
    private String profession;
}
