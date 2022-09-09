package com.fara.demo.domain;


import com.fara.demo.domain.Question.BaseQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Table(name = "course")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Course {
    public Course(String courseName, Date startingDate, Date endingDate) {
        this.courseName = courseName;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;

    public Course(String courseName) {
        this.courseName = courseName;
    }



    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "course_students",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "students_id", referencedColumnName = "id"))
    private Set<User> students = new HashSet<>();

    @Column(name = "course_name", nullable = false)
    private String courseName;


    @Column(name = "starting_date", nullable = false)
    private Date startingDate = Date.valueOf(LocalDate.now());


    @Column(name = "ending_date", nullable = false)
    private Date endingDate = Date.valueOf(LocalDate.now().plusMonths(1));

    @OrderBy("examDate desc")
    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<Exam> exams;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<BaseQuestion> baseQuestions;

    @PrePersist
    public void prePersist() throws Exception {
        validateDate();
        validateRoles();
    }

    private void validateDate() throws Exception {
        if (startingDate != null && endingDate != null) {
            if (endingDate.before(startingDate)) throw new Exception("ending date must be after starting date");
            if (endingDate.getTime() == startingDate.getTime())
                throw new Exception("start and end cant be at same time");
        }

    }

    public void setStudents(Set<User> students) throws Exception {
        validateRoles();
        this.students = students;
    }

    public void setStudent(User student) throws Exception {
        validateRoles();
        boolean add = this.students.add(student);
        if (!add) throw new Exception(" already is assigned");
    }

    public void setTutor(User tutor) throws Exception {
        validateRoles();
        this.tutor = tutor;
    }

    public void setStartingDate(Date startingDate) throws Exception {
        validateDate();
        this.startingDate = startingDate;

    }

    public void setEndingDate(Date endingDate) throws Exception {
        validateDate();
        this.endingDate = endingDate;
    }

    private void validateRoles() throws Exception {
        if (tutor != null) {
            if (tutor.getRoles() != Roles.TUTOR) throw new Exception(" tutor role is invalid");
            if (!tutor.getEnable()) throw new Exception(" tutor role is disabled");
        }
        AtomicBoolean error = new AtomicBoolean(false);
        if (students != null && !students.isEmpty())
            students.forEach(user -> {
                if (user.getRoles() != Roles.STUDENT) error.set(true);
                if (!user.getEnable()) error.set(true);
            });
        if (error.get()) throw new Exception(" students role invalid ");
    }

    @PreUpdate
    public void preUpdate() throws Exception {
        System.out.println("prrrrrrrrrrrrrrrrrrrreeee");
        validateRoles();
    }
}