package com.ruki.research_dev.dto_mapper;

import com.ruki.research_dev.dto.EmployeeResponseDTO;
import com.ruki.research_dev.dto.UserResponseDTO;
import com.ruki.research_dev.entity.Employee;
import com.ruki.research_dev.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DTOMapper {

    List<EmployeeResponseDTO> toEmployeeResponseDTOs (List<Employee> employees);

    List<UserResponseDTO> toUserResponseDTOs (List<User> users);
}
