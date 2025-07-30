package com.abc.student.controller;

import com.abc.student.dto.StudentDTO;
import com.abc.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: akash
 * Date: 30/7/25
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping
    public ResponseEntity<StudentDTO> update(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
