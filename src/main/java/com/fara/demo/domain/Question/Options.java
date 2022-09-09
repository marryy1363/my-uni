package com.fara.demo.domain.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// option is preserved and throws error in syntax of sql
// so we use option(s)
public class Options {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private MultiOptionQuestion question;

    public Options(String text) {
        this.text = text;
    }

    public Options(String text, MultiOptionQuestion question) {
        this.text = text;
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Options options = (Options) o;
        return id != null && Objects.equals(id, options.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}