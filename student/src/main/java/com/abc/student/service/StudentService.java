package com.abc.student.service;

import com.abc.student.dto.StudentDTO;

import java.util.List;

/**
 * Author: akash
 * Date: 30/7/25
 */
public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(Long id);
    StudentDTO updateStudent(StudentDTO studentDTO);
    void deleteStudent(Long id);
}
