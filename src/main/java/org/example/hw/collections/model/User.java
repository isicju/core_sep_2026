package org.example.hw.collections.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@Data
public class User {
    private final Long id;
    private final String name;
    private final LocalDate birthDate;
    private final String city;
    private final String address;
    private final String email;
    private final String phone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}



