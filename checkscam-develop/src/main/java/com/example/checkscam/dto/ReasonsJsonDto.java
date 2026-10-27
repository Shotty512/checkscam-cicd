package com.example.checkscam.dto;

import lombok.*;

import java.util.List;

@Data
public class ReasonsJsonDto {
    private List<Reason> reasons;

    @Data
    public static class Reason{
        private String name;
        private Integer quantity;

        public boolean isExitByName(String name) {
            return this.name.equals(name);
        }
    }
}
