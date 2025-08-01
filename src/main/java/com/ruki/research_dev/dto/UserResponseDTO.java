package com.ruki.research_dev.dto;

import com.ruki.research_dev.entity.Role;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.ruki.research_dev.entity.User}
 */
@Value
public class UserResponseDTO implements Serializable {
    Integer id;
    String username;
    String password;
    EmployeeDto employee;
    Set<UserRoleDto> userRoles;

    /**
     * DTO for {@link com.ruki.research_dev.entity.Employee}
     */
    @Value
    public static class EmployeeDto implements Serializable {
        Integer id;
        String name;
        String nic;
    }

    /**
     * DTO for {@link com.ruki.research_dev.entity.UserRole}
     */
    @Value
    public static class UserRoleDto implements Serializable {
        Integer id;
        Role role;
    }
}
