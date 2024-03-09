package com.example.demo.school;

import org.springframework.stereotype.Service;

@Service
public class SchoolMapper {
    public SchoolDto toSchoolDto(SchoolEntity entity) {
        return new SchoolDto(entity.getName());
    }

    public SchoolEntity toSchool(SchoolDto dto) {
        var school = new SchoolEntity();
        school.setName(dto.name());
        return school;
    }
}
