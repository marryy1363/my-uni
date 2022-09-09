package com.fara.demo.repository;



import com.fara.demo.domain.answer.EssayAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface EssayAnswerRepository extends JpaRepository<EssayAnswer, Long> {
    @Query(value = "select b from BaseStudentAnswer b where b.student.id = :studentId and b.examQuestionsPoints.exam.id = :examId")
    List<EssayAnswer> findByStudentAndExam(@Param("studentId") Long studentId, @Param("examId") Long examId);
}
