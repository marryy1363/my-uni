package com.fara.demo.repository;



import com.fara.demo.domain.Course;
import com.fara.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTutor(User tutor);
    List<Course> findByStudentsEquals(User student);
    List<Course> findByStudentsIsContaining(User student);

}
