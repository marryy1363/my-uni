package com.fara.demo.service.impl;

import com.fara.demo.domain.Course;
import com.fara.demo.domain.User;
import com.fara.demo.repository.CourseRepository;
import com.fara.demo.repository.UserRepository;
import com.fara.demo.service.AdminServiceApi;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements AdminServiceApi {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    public AdminService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void removeStudentFromCourse(Long studentId, Long courseId) throws Exception {
        Optional<User> byId = userRepository.findById(studentId);
        if (byId.isEmpty()) throw new Exception("student not found");
        Optional<Course> byId1 = courseRepository.findById(courseId);
        if (byId1.isEmpty()) throw new Exception("course not found");
        Course course = byId1.get();
        User student = byId.get();
        boolean remove = course.getStudents().remove(student);
        if (!remove) throw new Exception("cant remove");
        courseRepository.save(course);
    }

    @Override
    public void assignAnStudentToCourse(Long courseId, Long studentId) throws Exception {
        Optional<User> byId = userRepository.findById(studentId);
        if (byId.isEmpty()) throw new IllegalStateException(" no student found");
        User student = byId.get();
        Optional<Course> byId1 = courseRepository.findById(courseId);
        if (byId1.isEmpty()) throw new IllegalStateException("course not found");
        Course course = byId1.get();
        boolean add = course.getStudents().add(student);
        if (!add) throw new IllegalStateException(" already assigned ");
        courseRepository.save(course);
    }

    @Override
    public void assignTutorToCourse(Long courseId, Long tutorId) {
        Optional<Course> byId1 = courseRepository.findById(courseId);
        if (byId1.isEmpty()) throw new IllegalStateException("course not found");
        Course course = byId1.get();

        Optional<User> byId = userRepository.findById(tutorId);
        if (byId.isEmpty()) throw new IllegalStateException(" no student found");
        User tutor = byId.get();
        if (course.getTutor()!=null)
        if (course.getTutor().getId() == tutorId)
            throw new IllegalStateException(" this tutor already assigned to this course before");
        try {
            course.setTutor(tutor);
            courseRepository.save(course);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }


    }
}
