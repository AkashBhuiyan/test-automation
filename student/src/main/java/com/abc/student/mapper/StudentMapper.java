package com.abc.student.mapper;

import com.abc.student.dto.StudentDTO;
import com.abc.student.entity.Student;

/**
 * Author: akash
 * Date: 30/7/25
 */
public class StudentMapper {

    public static StudentDTO toDto(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .age(student.getAge())
                .build();
    }

    public static Student toEntity(StudentDTO dto) {
        return Student.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();
    }
}

