package org.example;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SalaryRecordParser {
    List<SalaryRecord> parse(File file) throws IOException;
}