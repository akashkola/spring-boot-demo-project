package com.example.demo.student;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/students")
    public StudentResponseDto create(@Valid @RequestBody StudentDto dto) {
        return studentService.saveStudent(dto);
    }

    @GetMapping("/students/{id}")
    public StudentResponseDto getStudent(@PathVariable Integer id) {
        return studentService.findStudentById(id);
    }

    @GetMapping("/students")
    public List<StudentResponseDto> getStudents() {
        return studentService.findAllStudents();
    }

    @GetMapping("/students/search/{student-name}")
    public List<StudentResponseDto> getStudents(@PathVariable("student-name") String name) {
        return studentService.findStudentsByName(name);
    }

    @DeleteMapping("/students/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudentById(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError)error).getField();
                    var errorMsg = error.getDefaultMessage();
                    errors.put(fieldName, errorMsg);
                });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
