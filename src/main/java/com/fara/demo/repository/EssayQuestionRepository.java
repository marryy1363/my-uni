package com.fara.demo.repository;



import com.fara.demo.domain.Question.EssayQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EssayQuestionRepository extends JpaRepository<EssayQuestion, Long> {
    List<EssayQuestionRepository> findByTitleLike(String title);

}
