package com.example.ProductConsumer.Model;

import java.util.LinkedList;
import java.util.Queue;

public class Queues {

    private String id;                //Id for each queue
    private Queue<Product> products; // the real queue of products
    private int initialImportRate;  // import rate used for the initial queue

    public Queues(String id, int initialImportRate) {
        this.id = id;
        this.products=new LinkedList<>();
        this.initialImportRate=initialImportRate;
    }
    public Queues(String id, int initialImportRate ,Queue<Product> products) {
        this.id = id;
        this.products=products;
        this.initialImportRate=initialImportRate;
    }

    public int getInitialImportRate() {
        return initialImportRate;
    }


    public void setInitialImportRate(int initialImportRate) {
        this.initialImportRate = initialImportRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Queue<Product> getProducts() {
        return products;
    }

    public void setProducts(Queue<Product> products) {
        this.products = products;
    }

    public synchronized String enqueue (Product processedProduct){
        this.products.add(processedProduct);
        System.out.println(processedProduct+" Enqueued Successfully "+ this.products.size());
        return "Enqueued Successfully";
    }

    public synchronized Product dequeue(){
        return this.products.poll();
    }

    public synchronized int size(){
        return this.products.size();
    }

}