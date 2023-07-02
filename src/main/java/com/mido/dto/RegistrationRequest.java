package com.mido.dto;

import com.mido.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String image;
    private List<Role> roles;
}
