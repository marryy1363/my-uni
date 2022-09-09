package com.fara.demo.service.impl;

;
import com.fara.demo.domain.Course;
import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.repository.CourseRepository;
import com.fara.demo.repository.UserRepository;
import com.fara.demo.service.CourseServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Optional;

import static com.fara.demo.Utils.DateUtils.dateOf;


@Service
public class CourseService implements CourseServiceApi {


    final CourseRepository courseRepository;
    final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Course assignATutorToTheCourse(Course course, User tutor) throws Exception {
        if (course.getTutor() == null) throw new Exception(" no tutor assigned");
        if (tutor.getId() == null) throw new Exception(" no valid tutor");
        Optional<User> byId =
                userRepository.findById(tutor.getId());
        if (byId.isEmpty()) throw new Exception(" no valid tutor");
        course.setTutor(byId.get());
        if (Roles.TUTOR != course.getTutor().getRoles()) throw new Exception("invalid role");
        if (course.getId() == null && course.getTutor().getId() != null) {
            return courseRepository.save(course);

        }
        return null;
    }

    @Override
    public Course addStudentToCourse(Course course, User student) throws Exception {
        if (Roles.STUDENT != student.getRoles()) throw new Exception("invalid role");
        if (course.getStudents().add(student)) return courseRepository.save(course);
        return null;
    }

    @Override
    public Course createACourse(String courseName, String starting, String ending) throws Exception {

        Date start = dateOf(starting);
        Date end = dateOf(ending);
        Course course = new Course();
        course.setStartingDate(start);
        course.setEndingDate(end);
        course.setCourseName(courseName);
        courseRepository.save(course);
        return course;
    }
}
