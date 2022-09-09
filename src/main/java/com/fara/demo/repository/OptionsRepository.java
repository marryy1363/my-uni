package com.fara.demo.repository;



import com.fara.demo.domain.Question.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Long> {

}
