package com.fara.demo.repository;



import com.fara.demo.domain.Course;
import com.fara.demo.domain.Question.BaseQuestion;
import org.springframework.data.jpa.domain.Specification;

public class QuestionSpecification {
    public static Specification<BaseQuestion> type(String type) {
        if(type.equals("all"))
            return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    root.get("questionType"), "%%"
            );
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("questionType"), type
        );

    }

    public static Specification<BaseQuestion> title(String title) {
        return  (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get("title"), "%"+title+"%"
        );
    }

    public static Specification<BaseQuestion> courseIs(Course course) {
         return  (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("course"), course
        );
    }
}
