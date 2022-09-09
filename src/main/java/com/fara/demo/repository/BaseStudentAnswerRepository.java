package com.fara.demo.repository;


import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BaseStudentAnswerRepository extends JpaRepository<BaseStudentAnswer, Long> {
    List<BaseStudentAnswer> findByStudent(User student);
//    @Query(value = "select b from BaseStudentAnswer b where  b.examQuestionsPoints.exam.id = :examId")
//    List<BaseStudentAnswer> findByExamId(@Param("examId") Long examId);

    @Query(value = "select b from BaseStudentAnswer b where b.student = :student and b.examQuestionsPoints.exam.id = :examId")
    List<BaseStudentAnswer> findByStudentAndExam(@Param("student") User student, @Param("examId") Long examId);

    @Query(value = "select b from BaseStudentAnswer b where b.student.id = :studentId and b.examQuestionsPoints.exam.id = :examId")
    List<BaseStudentAnswer> findByStudentIdAndExamId(@Param("studentId") Long studentId, @Param("examId") Long examId);
}
