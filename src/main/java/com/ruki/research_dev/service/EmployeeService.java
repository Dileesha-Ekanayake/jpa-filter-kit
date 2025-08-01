package com.ruki.research_dev.service;

import com.ruki.research_dev.dto.EmployeeResponseDTO;

import java.util.HashMap;
import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDTO> getAllEmployees(HashMap<String, String> filters);
}
