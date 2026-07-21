package com.example.ch04.dto;

public record CityInfo(
        String city,
        String country,
        int population,
        String description
) {
}