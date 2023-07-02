package com.mido.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnUserDto {

    public ReturnUserDto(Long id, String fullName, String email, String username, String image,Long unread) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.image = image;
        this.unread = unread;
    }

    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String image;
    private Long unread;
}
