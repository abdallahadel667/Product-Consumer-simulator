package com.example.ProductConsumer.DTOs;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;

@AutoConfiguration
@Component
public class ResponseDTO {

   private String id;
   private String []colour;
   private int numberOfElements;

    public ResponseDTO( String id, String[] colour, int numberOfElements) {
        this.id = id;
        this.colour = colour;
        this.numberOfElements = numberOfElements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getColour() {
        return colour;
    }

    public void setColour(String[] colour) {
        this.colour = colour;
    }
    public int getNumberOfElements() {
        return numberOfElements;
    }
    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
