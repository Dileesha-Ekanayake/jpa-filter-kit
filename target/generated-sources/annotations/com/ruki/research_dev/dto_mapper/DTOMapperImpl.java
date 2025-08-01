package com.ruki.research_dev.dto_mapper;

import com.ruki.research_dev.dto.EmployeeResponseDTO;
import com.ruki.research_dev.dto.UserResponseDTO;
import com.ruki.research_dev.entity.Employee;
import com.ruki.research_dev.entity.Gender;
import com.ruki.research_dev.entity.Role;
import com.ruki.research_dev.entity.User;
import com.ruki.research_dev.entity.UserRole;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-01T18:44:03+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class DTOMapperImpl implements DTOMapper {

    @Override
    public List<EmployeeResponseDTO> toEmployeeResponseDTOs(List<Employee> employees) {
        if ( employees == null ) {
            return null;
        }

        List<EmployeeResponseDTO> list = new ArrayList<EmployeeResponseDTO>( employees.size() );
        for ( Employee employee : employees ) {
            list.add( employeeToEmployeeResponseDTO( employee ) );
        }

        return list;
    }

    @Override
    public List<UserResponseDTO> toUserResponseDTOs(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<UserResponseDTO>( users.size() );
        for ( User user : users ) {
            list.add( userToUserResponseDTO( user ) );
        }

        return list;
    }

    protected EmployeeResponseDTO employeeToEmployeeResponseDTO(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        Integer id = null;
        String name = null;
        String nic = null;
        LocalDate dobirth = null;
        Gender gender = null;

        id = employee.getId();
        name = employee.getName();
        nic = employee.getNic();
        dobirth = employee.getDobirth();
        gender = employee.getGender();

        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO( id, name, nic, dobirth, gender );

        return employeeResponseDTO;
    }

    protected UserResponseDTO.UserRoleDto userRoleToUserRoleDto(UserRole userRole) {
        if ( userRole == null ) {
            return null;
        }

        Integer id = null;
        Role role = null;

        id = userRole.getId();
        role = userRole.getRole();

        UserResponseDTO.UserRoleDto userRoleDto = new UserResponseDTO.UserRoleDto( id, role );

        return userRoleDto;
    }

    protected Set<UserResponseDTO.UserRoleDto> userRoleSetToUserRoleDtoSet(Set<UserRole> set) {
        if ( set == null ) {
            return null;
        }

        Set<UserResponseDTO.UserRoleDto> set1 = new LinkedHashSet<UserResponseDTO.UserRoleDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserRole userRole : set ) {
            set1.add( userRoleToUserRoleDto( userRole ) );
        }

        return set1;
    }

    protected UserResponseDTO.EmployeeDto employeeToEmployeeDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        Integer id = null;
        String name = null;
        String nic = null;

        id = employee.getId();
        name = employee.getName();
        nic = employee.getNic();

        UserResponseDTO.EmployeeDto employeeDto = new UserResponseDTO.EmployeeDto( id, name, nic );

        return employeeDto;
    }

    protected UserResponseDTO userToUserResponseDTO(User user) {
        if ( user == null ) {
            return null;
        }

        Set<UserResponseDTO.UserRoleDto> userRoles = null;
        Integer id = null;
        String username = null;
        String password = null;
        UserResponseDTO.EmployeeDto employee = null;

        userRoles = userRoleSetToUserRoleDtoSet( user.getUserRoles() );
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        employee = employeeToEmployeeDto( user.getEmployee() );

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, username, password, employee, userRoles );

        return userResponseDTO;
    }
}
