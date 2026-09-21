package org.example.hw.functional.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Department {
    public int departmentId;
    public String departmentName;
    public int managerId;
    public int locationId;
}
 