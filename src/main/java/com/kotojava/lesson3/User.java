package com.kotojava.lesson3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String city;
    private String address;
    private String email;
    private String phone;

}



