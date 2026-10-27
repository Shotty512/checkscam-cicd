package com.example.checkscam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String role; // "user", "assistant", "system"
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // getters/setters
}