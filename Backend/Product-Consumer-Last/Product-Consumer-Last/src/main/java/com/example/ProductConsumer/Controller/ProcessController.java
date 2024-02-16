package com.example.ProductConsumer.Controller;

import com.example.ProductConsumer.DTOs.ResponseDTO;
import com.example.ProductConsumer.Model.Machine;
import com.example.ProductConsumer.Model.Memento;
import com.example.ProductConsumer.Model.Product;
import com.example.ProductConsumer.Model.Queues;

import java.util.*;

import static java.lang.Thread.*;


// Singleton class
public class ProcessController {
    private static ProcessController controller;
    private SystemAssembler assembler;
    // memento of the initial conditions
    private Memento initialState;

    public ProcessController() {
        this.assembler= SystemAssembler.getInstance();
        this.initialState =null;
    }
    public static ProcessController getInstance(){
        if(controller==null){
            controller= new ProcessController();
        }
        return controller;
    }

    public Memento getInitialState() {
        return initialState;
    }

    public void setInitialState(Memento initialState) {
        this.initialState = initialState;
    }

    //runs simulation
    public String run() {
        takeSnapshot() ;
        for (Machine machine : assembler.getMyMachines()) {
           new Thread(machine).start();
        }

        return "Simulation ran Successfully";
    }

    //updates simulation
    public LinkedList<ResponseDTO> update(){
        LinkedList<ResponseDTO> dto =new LinkedList<>();

        for (Machine machine : assembler.getMyMachines()) {
            if (machine.getCurrentProduct()==null){
                machine.setCurrentProduct(new Product(new String[]{"0", "128", "0"}));
                dto.add(new ResponseDTO(machine.getId(),machine.getCurrentProduct().getColor(),0));

            }
            dto.add(new ResponseDTO(machine.getId(),machine.getCurrentProduct().getColor(),0));
        }
        for (Queues queue : assembler.getMyQueues()){
            dto.add(new ResponseDTO(queue.getId(),null,queue.size()));
        }
        return dto;
    }

    public String replay() {
        // returning initial conditions from memento taken
        int k =0;
        assembler.setMyQueues(initialState.getQueues());

        for (Machine machine:assembler.getMyMachines()){


            machine.setEndQueue(initialState.getMachines().get(k).getEndQueue());
            machine.setEndQueue(new Queues(machine.getEndQueue().getId(),machine.getEndQueue().getInitialImportRate()));
            machine.setObservingQueue(initialState.getMachines().get(k).getObservingQueue());
            machine.setDumpingQueue(initialState.getMachines().get(k).getDumpingQueue());
            machine.setServiceTime(initialState.getMachines().get(k).getServiceTime());
            machine.setId(initialState.getMachines().get(k).getId());
            machine.setReady(initialState.getMachines().get(k).isReady());
            machine.setId(initialState.getMachines().get(k).getId());
            k++;
        }
        k=0;
        for(Queues queue: assembler.getMyQueues()){
            queue.setId(initialState.getQueues().get(k).getId());
            queue.setProducts(initialState.getQueues().get(k).getProducts());
            queue.setInitialImportRate(initialState.getQueues().get(k).getInitialImportRate());
            k++;
        }



        for (Product product : assembler.getMyProducts()) {
            assembler.getMyMachines().get(0).getObservingQueue().get(0).enqueue(product);
        }

        return "Simulation replayed successfully";
    }

    public void takeSnapshot () {
        Memento memento = new Memento(assembler.machineCopy(),
               assembler.queueCopy(), (LinkedList<Product>) assembler.getMyProducts().clone());
        setInitialState(memento);
    }

}