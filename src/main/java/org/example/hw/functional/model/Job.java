package org.example.hw.functional.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Job {
    public String jobId;
    public String jobTitle;
    public double minSalary;
    public double maxSalary;
}
