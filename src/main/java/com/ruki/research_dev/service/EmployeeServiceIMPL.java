package com.ruki.research_dev.service;

import com.ruki.research_dev.dto.EmployeeResponseDTO;
import com.ruki.research_dev.dto_mapper.DTOMapper;
import com.ruki.research_dev.entity.Employee;
import com.ruki.research_dev.repository.EmployeeRepository;
import lk.dileesha.jpafilter.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceIMPL implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DTOMapper dtoMapper;
    private final SpecificationBuilder specificationBuilder;

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeResponseDTO> getAllEmployees(HashMap<String, String> filters) {
        List<EmployeeResponseDTO> employeeResponseDTOS = dtoMapper.toEmployeeResponseDTOs(employeeRepository.findAll());

        if (filters == null || filters.isEmpty()) {
            return employeeResponseDTOS;
        }

        Specification<Employee> specification = specificationBuilder.createFilterSpecifications(filters);
        return dtoMapper.toEmployeeResponseDTOs(employeeRepository.findAll(specification));
    }
}
