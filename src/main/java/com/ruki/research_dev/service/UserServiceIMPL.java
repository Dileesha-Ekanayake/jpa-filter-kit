package com.ruki.research_dev.service;

import com.ruki.research_dev.dto.UserResponseDTO;
import com.ruki.research_dev.dto_mapper.DTOMapper;
import com.ruki.research_dev.entity.User;
import com.ruki.research_dev.repository.UserRepository;
import lk.dileesha.jpafilter.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService{

    private final UserRepository userRepository;
    private final DTOMapper dtoMapper;
    private final SpecificationBuilder specificationBuilder;

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDTO> getAllUsers(HashMap<String, String> filters) {
        List<UserResponseDTO> userResponseDTOS = dtoMapper.toUserResponseDTOs(userRepository.findAll());

        if (filters == null || filters.isEmpty()) {
            return userResponseDTOS;
        }

        Specification<User> specification = specificationBuilder.createFilterSpecifications(filters);
        return dtoMapper.toUserResponseDTOs(userRepository.findAll(specification));
    }
}
