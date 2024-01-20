package com.example.productConsumer.Model;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class Product {

    private final String [] color;

    public Product() {
        this.color=setColor();
    }

   private String[] setColor() {

       int r = ThreadLocalRandom.current().nextInt(0, 255);
       int g = ThreadLocalRandom.current().nextInt(0, 255);
       int b = ThreadLocalRandom.current().nextInt(0, 255);

       return new String[]{String.valueOf(r),String.valueOf(g),String.valueOf(b)};
   }


}
