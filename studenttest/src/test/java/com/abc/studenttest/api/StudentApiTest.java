package com.abc.studenttest.api;

import com.abc.studenttest.dto.StudentDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: akash
 * Date: 30/7/25
 */

public class StudentApiTest {

    private static RestTemplate restTemplate;
    private static JdbcTemplate jdbcTemplate;
    private static final String BASE_URL = "http://localhost:8081/api/students";
    private StudentDTO input;

    @BeforeAll
    static void setup() {
        restTemplate = new RestTemplate();
        DataSource dataSource = new DriverManagerDataSource(
                "jdbc:mysql://localhost:3309/student?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "root"
        );
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void setUp() {
        input = StudentDTO.builder()
                .firstName("Leo")
                .lastName("Messi")
                .email("leo@yopmail.com")
                .age(22)
                .build();
    }

    @Test
    void testCreateStudentAndCheckInDB() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StudentDTO> request = new HttpEntity<>(input, headers);

        ResponseEntity<StudentDTO> response = restTemplate.postForEntity(BASE_URL, request, StudentDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDTO actual = response.getBody();

        assertStudentEquals(input, actual);

        assertStudentInDbEquals(actual.getId(), input);
    }

    @Test
    void testUpdateStudentAndCheckInDB() {

        StudentDTO created = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(input), StudentDTO.class).getBody();

        created.setFirstName("Neymar");
        created.setAge(30);

        HttpEntity<StudentDTO> request = new HttpEntity<>(created);
        ResponseEntity<StudentDTO> updated = restTemplate.exchange(BASE_URL, HttpMethod.PUT, request, StudentDTO.class);

        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDTO actual = updated.getBody();

        assertStudentEquals(created, actual);
        assertThat(created.getId()).isEqualTo(actual.getId());

        assertStudentInDbEquals(actual.getId(), created);
    }


    @Test
    void testGetAllStudentsAndCheckInDB() {
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(BASE_URL, StudentDTO[].class);

        Integer countInDb = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students", Integer.class);
        assertThat(response.getBody().length).isEqualTo(countInDb);
    }


    @Test
    void testGetStudentByIdAndCheckInDB() {

        StudentDTO created = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(input), StudentDTO.class).getBody();
        assertThat(created).isNotNull();
        Long id = created.getId();
        assertThat(id).isNotNull();

        StudentDTO fetched = restTemplate.getForEntity(BASE_URL + "/" + id, StudentDTO.class).getBody();
        assertThat(fetched).isNotNull();

        assertStudentInDbEquals(id, fetched);
    }

    @Test
    void testDeleteStudentAndCheckInDB() {

        StudentDTO created = restTemplate
                .postForEntity(BASE_URL, new HttpEntity<>(input), StudentDTO.class)
                .getBody();

        assertThat(created).isNotNull();
        Long id = created.getId();
        assertThat(id).isNotNull();

        restTemplate.delete(BASE_URL + "/" + id);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE id = ?",
                new Object[]{id},
                Integer.class
        );

        assertThat(count).isZero();
    }


    private void assertStudentEquals(StudentDTO expected, StudentDTO actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();

        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    private void assertStudentInDbEquals(Long id, StudentDTO expected) {
        Map<String, Object> dbResult = jdbcTemplate.queryForMap("SELECT * FROM students WHERE id = ?", id);

        assertThat(dbResult.get("email")).isEqualTo(expected.getEmail());
        assertThat(dbResult.get("age")).isEqualTo(expected.getAge());
        assertThat(dbResult.get("first_name")).isEqualTo(expected.getFirstName());
        assertThat(dbResult.get("last_name")).isEqualTo(expected.getLastName());
    }

}
