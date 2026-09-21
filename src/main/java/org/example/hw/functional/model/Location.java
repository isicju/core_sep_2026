package org.example.hw.functional.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    public int locationId;
    public String location;
}