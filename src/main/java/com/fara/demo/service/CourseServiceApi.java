package com.fara.demo.service;


import com.fara.demo.domain.Course;
import com.fara.demo.domain.User;
import org.springframework.stereotype.Service;


@Service
public interface CourseServiceApi {
//    Course createACourseForTutor(Course course) throws Exception;

    Course assignATutorToTheCourse(Course course, User tutor) throws Exception;

    Course addStudentToCourse(Course course, User student) throws Exception;

    Course createACourse(String courseName, String starting, String ending) throws Exception;
}
