package com.example.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSuccessfullySaveStudent() {
        // Given
        StudentDto studentDto = new StudentDto("firstName", "lastName", "email", 12);

        // Mocks
        StudentEntity mockStudent = new StudentEntity("firstName", "lastName", "email", 20);
        when(studentMapper.studentDtoToStudent(studentDto)).thenReturn(mockStudent);

        StudentEntity mockSavedStudent = new StudentEntity("firstName", "lastName", "email", 20);
        mockSavedStudent.setId(1);
        when(studentRepository.save(mockStudent)).thenReturn(mockSavedStudent);

        StudentResponseDto expectedResponseDto = new StudentResponseDto("firstName", "lastName", "email");
        when(studentMapper.studentToStudentResponseDto(mockSavedStudent)).thenReturn(expectedResponseDto);

        // When
        StudentResponseDto responseDto = studentService.saveStudent(studentDto);

        // Then
        assertEquals(expectedResponseDto.firstName(), responseDto.firstName());
        assertEquals(expectedResponseDto.lastName(), responseDto.lastName());
        assertEquals(expectedResponseDto.email(), responseDto.email());

        verify(studentRepository, times(1)).save(mockStudent);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfDtoIsNull() {
        // Given
        StudentDto dto = null;
        String expectedError = "the Student DTO should not be null";

        // When And Then
        var exception = assertThrows(NullPointerException.class, () -> studentService.saveStudent(dto));
        assertEquals(expectedError, exception.getMessage());

        verify(studentRepository, times(0)).save(any(StudentEntity.class));
    }

    @Test
    public void testFindAllStudents() {
        // Mocks
        List<StudentEntity> allStudents = new ArrayList<>();
        allStudents.add(new StudentEntity("firstName1", "lastName1", "email1", 10));
        allStudents.add(new StudentEntity("firstName2", "lastName2", "email2", 20));
        when(studentRepository.findAll()).thenReturn(allStudents);
        when(studentMapper.studentToStudentResponseDto(any(StudentEntity.class))).thenReturn(new StudentResponseDto("John", "Doe", "johndoe@gmail.com"));

        // When
        var students = studentService.findAllStudents();

        // Then
        assertEquals(allStudents.size(), students.size());
        students.forEach(student -> {
            assertEquals("John", student.firstName());
            assertEquals("Doe", student.lastName());
            assertEquals("johndoe@gmail.com", student.email());
        });

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testFindStudentByIdIfStudentExists() {
        // Given
        Integer studentId = 1;

        // Mocks
        var student = new StudentEntity("firstname", "lastname", "email", 10);
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.studentToStudentResponseDto(student)).thenReturn(new StudentResponseDto("firstname", "lastname", "email"));

        // When
        var retrievedStudent = studentService.findStudentById(studentId);

        // Then
        assertEquals(student.getFirstName(), retrievedStudent.firstName());
        assertEquals(student.getLastName(), retrievedStudent.lastName());
        assertEquals(student.getEmail(), retrievedStudent.email());

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfStudentIdIsNull() {
        // Given
        Integer nullStudentId = null;
        var expectedError = "student ID cannot be null";

        // When and Then
        var exception = assertThrows(NullPointerException.class, () -> studentService.findStudentById(nullStudentId));
        assertEquals(expectedError, exception.getMessage());

        verify(studentRepository, times(0)).findById(nullStudentId);
    }

    @Test
    public void shouldThrowExceptionIfStudentIdIsNotPositive() {
        // Given
        Integer invalidId = -1;
        var expectedError = "Student ID must be a positive number";

        // When and Then
        var exception = assertThrows(IdShouldBePositiveException.class, () -> studentService.findStudentById(invalidId));
        assertEquals(expectedError, exception.getMessage());

        verify(studentRepository, times(0)).findById(invalidId);
    }

    @Test
    public void checkIfStudentNotFoundByID() {
        //Given
        final Integer NOT_EXISTED_STUDENT_ID = 101;

        // Mocks
        when(studentRepository.findById(NOT_EXISTED_STUDENT_ID)).thenReturn(Optional.ofNullable(null));
        when(studentMapper.studentToStudentResponseDto(any(StudentEntity.class))).thenReturn(
                new StudentResponseDto("", "", "")
        );

        // When
        var actualStudent = studentService.findStudentById(NOT_EXISTED_STUDENT_ID);

        // Then
        assertEquals("", actualStudent.firstName());
        assertEquals("", actualStudent.lastName());
        assertEquals("", actualStudent.email());

        verify(studentRepository, times(1)).findById(NOT_EXISTED_STUDENT_ID);
    }

    @Test
    public void testFindStudentsByName() {
        // Given
        String name = "John";

        // Mocks
        List<StudentEntity> students = new ArrayList<>();
        students.add(new StudentEntity("Johnnie", "Doe", "email", 20));
        when(studentRepository.findAllByFirstNameContaining(name)).thenReturn(students);
        when(studentMapper.studentToStudentResponseDto(any(StudentEntity.class))).thenReturn(new StudentResponseDto("firstname", "lastname", "email"));

        // When
        var matchedStudents = studentService.findStudentsByName(name);

        // Then
        assertEquals(1, matchedStudents.size());

        verify(studentRepository, times(1)).findAllByFirstNameContaining(name);
    }

    @Test
    public void shouldThrowNullPointerExceptionIfNameIsNull() {
        // Given
        String name = null;
        String expectedError = "name should not be null";

        // When and Then
        var exception = assertThrows(NullPointerException.class, () -> studentService.findStudentsByName(name));
        assertEquals(expectedError, exception.getMessage());

        verify(studentRepository, times(0)).findAllByFirstNameContaining(name);
    }

    @Test
    public void shouldThrowExceptionIfNameIsEmpty() {
        // Given
        String name = "  ";
        String expectedError = "Name should not be blank";

        var exception = assertThrows(NameIsBlankException.class, () -> studentService.findStudentsByName(name));
        assertEquals(expectedError, exception.getMessage());

        verify(studentRepository, times(0)).findAllByFirstNameContaining(name);
    }

    @Test
    public void testDeleteStudentByIDIfStudentExists() {
        // Given
        Integer id = 1;

        // Mocks
        doNothing().when(studentRepository).deleteById(id);

        // When
        studentService.deleteStudentById(id);

        // Then
        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteStudentByIDIfIDIsInValid() {
        // Given
        Integer id = -1;
        String expectedError = "Student ID must be a positive number";

        // When and Then
        var exception = assertThrows(IdShouldBePositiveException.class, () -> studentService.deleteStudentById(id));
        assertEquals(expectedError, exception.getMessage());

        verify(studentRepository, times(0)).deleteById(id);
    }


}
