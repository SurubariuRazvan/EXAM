package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "Word")
public class Word implements com.exam.domain.Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "round_id")
    private Round round;

    @Column(name = "configuration")
    private String configuration;

    @Column(name = "generated_number")
    private Integer generatedNumber;

    public Word() {
    }

    public Word(Integer id, Student student, Round round, String configuration, Integer generatedNumber) {
        this.id = id;
        this.student = student;
        this.round = round;
        this.configuration = configuration;
        this.generatedNumber = generatedNumber;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Integer getGeneratedNumber() {
        return generatedNumber;
    }

    public void setGeneratedNumber(Integer generatedNumber) {
        this.generatedNumber = generatedNumber;
    }
}
