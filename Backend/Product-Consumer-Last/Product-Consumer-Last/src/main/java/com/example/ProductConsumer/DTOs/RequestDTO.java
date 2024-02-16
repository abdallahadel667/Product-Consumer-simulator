package com.example.ProductConsumer.DTOs;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;

@AutoConfiguration
@Component
public class RequestDTO {

    private int [][] architecture;

    public RequestDTO(int[][] architecture) {
        this.architecture = architecture;
    }
    public RequestDTO(){}


    public int[][] getArchitecture() {
        return architecture;
    }

    public void setArchitecture(int[][] architecture) {
        this.architecture = architecture;
    }
}
