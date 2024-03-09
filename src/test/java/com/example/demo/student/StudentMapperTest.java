package com.example.demo.student;

import com.example.demo.school.SchoolEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {
    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        this.studentMapper = new StudentMapper();
    }

    @Test
    public void shouldMapStudentDtoToStudent() {
        StudentDto dto = new StudentDto(
                "firstname",
                "lastname",
                "test@gmail.com",
                1
        );

        StudentEntity student = studentMapper.studentDtoToStudent(dto);

        assertEquals(dto.firstName(), student.getFirstName());
        assertEquals(dto.lastName(), student.getLastName());
        assertEquals(dto.email(), student.getEmail());
        assertNotNull(student.getSchool());
        assertEquals(dto.schoolId(), student.getSchool().getId());
    }

    @Test
    public void testShouldMapStudentToStudentResponseDto() {
        var student = new StudentEntity("firstname", "lastname", "email", 12);
        var dto = studentMapper.studentToStudentResponseDto(student);

        assertEquals(student.getFirstName(), dto.firstName());
        assertEquals(student.getLastName(), dto.lastName());
        assertEquals(student.getEmail(), dto.email());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenDtoIsNull() {
        var error = assertThrows(NullPointerException.class, () -> studentMapper.studentDtoToStudent(null));
        assertEquals("The student DTO should not be null", error.getMessage());
    }

}