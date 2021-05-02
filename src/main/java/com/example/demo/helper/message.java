package com.example.demo.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class message {
    private String content;
    private String type;

    public message() {
    }
}
