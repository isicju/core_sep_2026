package org.example.hw.collections.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


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

    public User(Long id, User source) {
        this.id = id;
        this.name = source.name;
        this.birthDate = source.birthDate;
        this.city = source.city;
        this.address = source.address;
        this.email = source.email;
        this.phone = source.phone;
    }
}



