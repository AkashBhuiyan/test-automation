package com.abc.student.service.impl;

import com.abc.student.dto.StudentDTO;
import com.abc.student.entity.Student;
import com.abc.student.mapper.StudentMapper;
import com.abc.student.repository.StudentRepository;
import com.abc.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: akash
 * Date: 30/7/25
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentDTO createStudent(StudentDTO dto) {
        Student student = StudentMapper.toEntity(dto);
        return StudentMapper.toDto(studentRepository.save(student));
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return StudentMapper.toDto(student);
    }

    @Override
    public StudentDTO updateStudent(StudentDTO dto) {
        Student existing = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getId()));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setAge(dto.getAge());

        return StudentMapper.toDto(studentRepository.save(existing));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
