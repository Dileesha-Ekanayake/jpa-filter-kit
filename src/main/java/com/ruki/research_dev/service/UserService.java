package com.ruki.research_dev.service;

import com.ruki.research_dev.dto.UserResponseDTO;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers(HashMap<String, String> filters);
}
