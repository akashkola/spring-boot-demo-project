package com.example.demo.student;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentResponseDto saveStudent(StudentDto dto) {
        if(dto == null) {
            throw new NullPointerException("the Student DTO should not be null");
        }
        var student = studentMapper.studentDtoToStudent(dto);
        var savedStudent = studentRepository.save(student);
        return studentMapper.studentToStudentResponseDto(savedStudent);
    }

    private void throwExceptionIfIdIsInvalid(Integer id) {
        if(id <= 0) {
            throw new IdShouldBePositiveException();
        }
    }

    public StudentResponseDto findStudentById(Integer id) {
        if(id == null) {
            throw new NullPointerException("student ID cannot be null");
        }
        throwExceptionIfIdIsInvalid(id);
        var student = studentRepository.findById(id).orElse(new StudentEntity());
        return studentMapper.studentToStudentResponseDto(student);
    }

    public List<StudentResponseDto> findAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::studentToStudentResponseDto)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDto> findStudentsByName(String name) {
        if(name == null) {
            throw new NullPointerException("name should not be null");
        }
        if(name.isBlank()) {
            throw new NameIsBlankException();
        }
        return studentRepository.findAllByFirstNameContaining(name)
                .stream()
                .map(studentMapper::studentToStudentResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteStudentById(Integer id) {
        throwExceptionIfIdIsInvalid(id);
        studentRepository.deleteById(id);
    }
}
