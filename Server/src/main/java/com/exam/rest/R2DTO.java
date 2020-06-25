package com.exam.rest;

import java.io.Serializable;
import java.util.List;

public class R2DTO implements Serializable {
    private String configuration;

    private Integer generatedNumber;

    public R2DTO(String configuration, Integer generatedNumber) {
        this.configuration = configuration;
        this.generatedNumber = generatedNumber;
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
