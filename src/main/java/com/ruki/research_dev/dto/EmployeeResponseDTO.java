package com.ruki.research_dev.dto;

import com.ruki.research_dev.entity.Gender;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ruki.research_dev.entity.Employee}
 */
@Value
public class EmployeeResponseDTO implements Serializable {
    Integer id;
    String name;
    String nic;
    LocalDate dobirth;
    Gender gender;
}
