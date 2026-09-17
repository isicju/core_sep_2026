package org.example.model;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String UUID;
    private String name;
    private int age;
    private int salary;
    private Profession profession;
}
