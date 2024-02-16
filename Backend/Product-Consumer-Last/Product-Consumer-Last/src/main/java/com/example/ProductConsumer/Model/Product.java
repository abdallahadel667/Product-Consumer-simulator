package com.example.ProductConsumer.Model;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@AutoConfiguration

public class Product {

    private  String [] color;

    public Product(String[] strings) {
        this.color=strings;
    }

    public String[] getColor() {
        return color;
    }

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