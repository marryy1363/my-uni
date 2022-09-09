package com.fara.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface AdminServiceApi {

    void removeStudentFromCourse(Long studentId, Long courseId) throws Exception;

    void assignAnStudentToCourse(Long courseId, Long studentId) throws Exception;


    void assignTutorToCourse(Long courseId, Long tutorId) throws Exception;
}
