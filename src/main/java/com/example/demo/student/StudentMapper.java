package com.example.demo.student;

import com.example.demo.school.SchoolEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    public StudentResponseDto studentToStudentResponseDto(StudentEntity student) {
        return new StudentResponseDto(student.getFirstName(), student.getLastName(), student.getEmail());
    }
    public StudentEntity studentDtoToStudent(StudentDto dto) {
        if(dto == null) {
            throw new NullPointerException("The student DTO should not be null");
        }
        var student = new StudentEntity();
        student.setFirstName(dto.firstName());
        student.setLastName(dto.lastName());
        student.setEmail(dto.email());

        var school = new SchoolEntity();
        school.setId(dto.schoolId());
        student.setSchool(school);

        return student;
    }
}
