package com.exam.domain;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "Letter_set")
public class LetterSet implements Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "configuration")
    private String configuration;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "configuration")
    private Set<Round> round;

    public LetterSet() {
    }

    public LetterSet(Integer id, String configuration, Set<Round> round) {
        this.id = id;
        this.configuration = configuration;
        this.round = round;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Set<Round> getRound() {
        return round;
    }

    public void setRound(Set<Round> round) {
        this.round = round;
    }
}
