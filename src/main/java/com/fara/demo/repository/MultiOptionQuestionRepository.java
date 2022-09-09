package com.fara.demo.repository;



import com.fara.demo.domain.Question.MultiOptionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiOptionQuestionRepository extends JpaRepository<MultiOptionQuestion, Long> {

    List<MultiOptionQuestionRepository> findByTitleLike(String title);


}
