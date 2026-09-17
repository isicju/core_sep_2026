package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String UUID;
    private String name;
    private int age;
    private int salary;
    private Profession profession;
}
