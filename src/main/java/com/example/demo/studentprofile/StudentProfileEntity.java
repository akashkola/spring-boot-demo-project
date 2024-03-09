package com.example.demo.studentprofile;

import com.example.demo.student.StudentEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "t_student_profile")
public class StudentProfileEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String bio;
    @OneToOne(mappedBy = "studentProfile")
    private StudentEntity student;

    public StudentProfileEntity() {
    }

    public StudentProfileEntity(String bio) {
        this.bio = bio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
