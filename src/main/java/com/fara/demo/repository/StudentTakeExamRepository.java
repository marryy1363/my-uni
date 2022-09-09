package com.fara.demo.repository;



import com.fara.demo.domain.Exam;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.StudentTakeExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTakeExamRepository extends JpaRepository<StudentTakeExam, Long> {
    StudentTakeExam findByExamAndUser(Exam exam, User user);

    List<StudentTakeExam> findByExam(Exam exam);

}
