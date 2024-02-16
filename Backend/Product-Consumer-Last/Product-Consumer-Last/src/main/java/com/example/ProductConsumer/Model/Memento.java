package com.example.ProductConsumer.Model;


import java.util.LinkedList;

public class Memento {

        private final LinkedList<Machine> machines;
        private final LinkedList<Queues> queues;
        private final LinkedList<Product> products;

        public Memento(LinkedList<Machine> machines, LinkedList<Queues> queues, LinkedList<Product> products) {
            this.machines = machines;
            this.queues = queues;
            this.products = products;
        }

    public LinkedList<Queues> getQueues() {
        return queues;
    }

    public LinkedList<Machine> getMachines() {
            return machines;
        }

    public LinkedList<Product> getProducts() {
        return products;
    }
}

