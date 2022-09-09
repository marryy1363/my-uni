package com.fara.demo.repository;


import com.fara.demo.domain.Course;
import com.fara.demo.domain.Question.BaseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface QuestionRepository extends JpaRepository<BaseQuestion, Long>, JpaSpecificationExecutor<BaseQuestion> {
    List<BaseQuestion> findByCourseId(long courseId);

    Boolean existsByTitle(String title);

    List<BaseQuestion> findByTitleLikeAndCourse(String title, Course course);

    List<BaseQuestion> findByQuestionTypeAndCourseAndTitleLike(String questionType, Course course, String title);


}
