package com.abc.student.repository;

import com.abc.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: akash
 * Date: 30/7/25
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
