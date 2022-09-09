package com.fara.demo.domain.Question;


import com.fara.demo.domain.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("EssayQuestion")
public class EssayQuestion extends BaseQuestion {
    public static String name = "EssayQuestion";

    public EssayQuestion(String title, Course course, String prompt) {
        super(title, course, prompt);
    }

}
