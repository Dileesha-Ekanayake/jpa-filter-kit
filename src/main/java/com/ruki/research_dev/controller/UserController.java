package com.ruki.research_dev.controller;

import com.ruki.research_dev.dto.UserResponseDTO;
import com.ruki.research_dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(required = false) HashMap<String, String> filters) {
        List<UserResponseDTO> userResponseDTOS = userService.getAllUsers(filters);
        return ResponseEntity.ok(userResponseDTOS);
    }
}
