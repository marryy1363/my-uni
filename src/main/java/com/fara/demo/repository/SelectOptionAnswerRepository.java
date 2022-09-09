package com.fara.demo.repository;


import com.fara.demo.domain.answer.SelectOptionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SelectOptionAnswerRepository extends JpaRepository<SelectOptionAnswer, Long> {
}
