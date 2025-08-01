package com.ruki.research_dev.controller;

import com.ruki.research_dev.dto.EmployeeResponseDTO;
import com.ruki.research_dev.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(@RequestParam(required = false) HashMap<String, String> filters) {
        List<EmployeeResponseDTO> employeeResponseDTOS = employeeService.getAllEmployees(filters);
        return ResponseEntity.ok(employeeResponseDTOS);
    }
}
