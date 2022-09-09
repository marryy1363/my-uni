package com.fara.demo.domain.Question;



import com.fara.demo.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("MultiOptionQuestion")
public class MultiOptionQuestion extends BaseQuestion {


    @OneToMany(mappedBy = "question", orphanRemoval = false, fetch = FetchType.EAGER)
    private Set<Options> opts = new HashSet<>();

    public void setAnswerSafe(Options answer) {
        if (opts.contains(answer)) {
            System.out.println("contains ok");
            this.answer = answer;
        }
        if(!opts.contains(answer))
        throw new IllegalStateException(" answer must be in options");
    }

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_opt_id")
    private Options answer;

    public boolean addOpt(Options opt) {
        return opts.add(opt);
    }

    public MultiOptionQuestion(String title, Course course, String prompt) {
        super(title, course, prompt);
    }
}
