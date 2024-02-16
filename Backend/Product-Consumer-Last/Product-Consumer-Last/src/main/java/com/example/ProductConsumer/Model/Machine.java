package com.example.ProductConsumer.Model;

import com.example.ProductConsumer.Controller.ProcessController;
import com.example.ProductConsumer.Controller.SystemAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Component
@AutoConfiguration
public class Machine implements Runnable{



    //List of observers
    private ArrayList<Queues> observingQueue;
    // queues in which machines dump products after processing
    private Queues dumpingQueue;
    private String id;//machine id
    private Product currentProduct; //product processed by machine
    private int serviceTime;
    private boolean ready;   // status of the machine true-> Ready to process , false -> busy

    private Queues endQueue;
    @Autowired
    public Machine(ArrayList<Queues> observingQueue, Queues receiverQueue, String id) {
        this.observingQueue = observingQueue;
        this.dumpingQueue = receiverQueue;
        this.id = id;
        this.currentProduct = null;
        this.serviceTime = ThreadLocalRandom.current().nextInt(5000, 10000);
        this.ready = true;
    }
    public Machine(ArrayList<Queues> observingQueue, Queues receiverQueue, String id, Product currentProduct,int serviceTime,Queues endQueue) {
        this.observingQueue = observingQueue;
        this.dumpingQueue = receiverQueue;
        this.id = id;
        this.currentProduct = currentProduct;
        this.serviceTime =serviceTime;
        this.ready = true;
        this.endQueue=endQueue;
    }
    public Machine( String id) {
        this.observingQueue = new ArrayList<Queues>();
        this.dumpingQueue = null;
        this.id = id;
        this.currentProduct = null;
        this.serviceTime = ThreadLocalRandom.current().nextInt(5000, 10000);
        this.ready = true;
    }

    public Machine (String id,int serviceTime,Queues endQueue,ArrayList<Queues> observers,Queues dump)
    {
        this.observingQueue = observers;
        this.dumpingQueue = dump;
        this.id = id;
        this.currentProduct = null;
        this.serviceTime = serviceTime;
        this.ready = true;
        this.endQueue=endQueue;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public ArrayList<Queues> getObservingQueue() {
        return observingQueue;
    }

    public void setObservingQueue(ArrayList<Queues> observingQueue) {
        this.observingQueue = observingQueue;
    }
    public Queues getDumpingQueue() {
        return dumpingQueue;
    }

    public void setDumpingQueue(Queues dumpingQueue) {
        this.dumpingQueue = dumpingQueue;
    }

    public Queues getEndQueue() {
        return endQueue;
    }

    public void setEndQueue(Queues endQueue) {
        this.endQueue = endQueue;
    }
    public ArrayList<Queues> addObservingQueue(Queues observing){
        this.observingQueue.add(observing);
        return this.observingQueue;
    }

@Override
public void run(){

        endQueue.getProducts().clear();
    try {
        while (this.endQueue.getProducts().size()!= this.endQueue.getInitialImportRate()){

            if(isReady()) {
                for(int i = 0 ; i < getObservingQueue().size() ; i++){
                    Queues currentObservingQueue = getObservingQueue().get(i);

                    if(currentObservingQueue.size()!=0){
                        System.out.println(this.id+" " +currentObservingQueue.getId());
                        System.out.println(this.id + ": " + this);
                        System.out.println("Before Dequeue: " + currentObservingQueue.size());
                        this.currentProduct = currentObservingQueue.dequeue();

                        System.out.println(this.id + ": Product:  " + this.currentProduct);
                        System.out.println("After Dequeue: " + currentObservingQueue.size());

                        System.out.print(this.id + " ");
                        if(currentProduct!=null){

                            setReady(false);
                            try {
                                sleep(this.serviceTime);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            this.dumpingQueue.enqueue(this.currentProduct);
                        }
                        // take snapshot here........
                        this.currentProduct = null;
                        setReady(true);
                    }
                }
            }
        }
    }
    catch (Exception ignored){

      }
    System.out.println(currentThread().isAlive());
   }


}