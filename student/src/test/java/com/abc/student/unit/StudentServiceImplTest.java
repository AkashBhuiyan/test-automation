package com.abc.student.unit;

import com.abc.student.dto.StudentDTO;
import com.abc.student.entity.Student;
import com.abc.student.mapper.StudentMapper;
import com.abc.student.repository.StudentRepository;
import com.abc.student.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Author: akash
 * Date: 30/7/25
 */
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = Student.builder()
                .id(1L)
                .firstName("Shafin")
                .lastName("Akter")
                .email("shafin@yopmail.com")
                .age(22)
                .build();

        studentDTO = StudentMapper.toDto(student);
    }

    @Test
    void shouldCreateStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.createStudent(studentDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(student.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentDTO> result = studentService.getAllStudents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        verify(studentRepository).findAll();
    }

    @Test
    void shouldReturnStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO result = studentService.getStudentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(studentRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenStudentNotFoundById() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void shouldUpdateStudent() {
        Student updated = Student.builder()
                .id(1L)
                .firstName("Will")
                .lastName("Smith")
                .email("smith@yopmail.com")
                .age(23)
                .build();

        StudentDTO updatedDTO = StudentMapper.toDto(updated);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updated);

        StudentDTO result = studentService.updateStudent(updatedDTO);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getEmail()).isEqualTo("jane@yopmail.com");
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudent(studentDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    void shouldDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingStudent() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");
    }
}
