package com.abc.studenttest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: akash
 * Date: 30/7/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;         // Optional during creation
    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
