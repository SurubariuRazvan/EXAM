package com.exam.rest;

import com.exam.domain.Student;

import java.io.Serializable;
import java.util.List;

public class R1DTO implements Serializable {
    private String configuration;

    private List<String> studentNames;

    public R1DTO(String configuration, List<String> studentNames) {
        this.configuration = configuration;
        this.studentNames = studentNames;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }
}
