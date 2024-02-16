package com.example.ProductConsumer.Controller;

import com.example.ProductConsumer.Model.Machine;
import com.example.ProductConsumer.Model.Product;
import com.example.ProductConsumer.Model.Queues;

import java.util.ArrayList;
import java.util.LinkedList;

//Singleton Class
public class SystemAssembler {
    private static SystemAssembler assembler;
    private int[][] architecture;
    private LinkedList<Machine> myMachines ;
    private LinkedList<Queues> myQueues;
    private LinkedList<Product> myProducts;
    private int initialNumberOfProducts;

    public LinkedList<Queues> queueCopy() {
        LinkedList<Queues> copied = new
                LinkedList<>();

        // Deep copy myQueues
        for (Queues originalQueue : myQueues) {
            Queues copiedQueue = new Queues(originalQueue.getId(), initialNumberOfProducts,originalQueue.getProducts());
            copied.add(copiedQueue);
        }
        return copied;
    }
    public LinkedList<Machine> machineCopy() {
        LinkedList<Machine> copied = new LinkedList<>();

        // Deep copy myQueues
        for (Machine machine : myMachines) {
            Machine copiedMachine=new Machine(machine.getObservingQueue(),machine.getDumpingQueue(),
                    machine.getId(),machine.getCurrentProduct(),machine.getServiceTime(),machine.getEndQueue());
            copied.add(copiedMachine);
        }
        return copied;
    }




    public SystemAssembler(){
        this.myMachines = new LinkedList<>();
        this.myQueues = new LinkedList<>();
        this.myProducts=new LinkedList<>();
    }

    public static SystemAssembler getInstance(){
        if (assembler == null){
            assembler= new SystemAssembler();
        }
        return assembler;
    }

    public int getInitialNumberOfProducts() {
        return initialNumberOfProducts;
    }

    public void setInitialNumberOfProducts(int initialNumberOfProducts) {
        this.initialNumberOfProducts = initialNumberOfProducts;
    }
    public int[][] getArchitecture() {
        return architecture;
    }

    public void setArchitecture(int[][] architecture) {
        this.architecture = architecture;
    }

    public LinkedList<Machine> getMyMachines() {
        return myMachines;
    }

    public void setMyMachines(LinkedList<Machine> myMachines) {
        this.myMachines = myMachines;
    }

    public LinkedList<Queues> getMyQueues() {
        return myQueues;
    }

    public void setMyQueues(LinkedList<Queues> myQueues) {
        this.myQueues = myQueues;
    }

    public LinkedList<Product> getMyProducts() {
        return myProducts;
    }

    public void setMyProducts(LinkedList<Product> myProducts) {
        this.myProducts = myProducts;
    }

    public LinkedList<Machine> simulationInitializer(int [][] architecture , int initialNumberOfProducts ){
        setArchitecture(architecture);
        setInitialNumberOfProducts(initialNumberOfProducts);
        int noOfMachines = architecture.length;
        int noOfQueues = architecture[0].length;
        for(int queues = 0; queues<noOfQueues; queues++){
            myQueues.add(new Queues("Q"+queues,initialNumberOfProducts));
        }

        for (int product = 0 ; product < initialNumberOfProducts ; product++ ){
            Product temp=new Product();
            myProducts.add(temp);
            myQueues.get(0).enqueue(temp);
        }
        for(int machine = 0; machine < noOfMachines ; machine++){
            Machine newMachine = new Machine("M"+machine);
            newMachine.setEndQueue(myQueues.get(EndQueue()));
            System.out.println(newMachine.getId()+" "+ newMachine.getServiceTime());
            myMachines.add(newMachine);
            ArrayList<Queues> observing = new ArrayList<>();
            for(int queue = 0 ; queue < noOfQueues ; queue++){
                switch (architecture[machine][queue]){
                    case 1://dumping
                        newMachine.setDumpingQueue(myQueues.get(queue));
                        break;
                    case -1://get supplied

                        newMachine.setObservingQueue(newMachine.addObservingQueue(myQueues.get((queue))));
                        break;
                    default:
                        break;
                }
            }
            System.out.println(newMachine.getId()+": "+newMachine);
        }

        return myMachines;
    }

    public int EndQueue(){
        int noOfMachines = architecture.length;
        int noOfQueues = architecture[0].length;

        for(int queues = 0 ; queues<noOfQueues ; queues++){
            boolean check = true;
            for (int[] ints : architecture) {
                if (ints[queues] == -1) {
                    check = false;
                }
            }
            if (check){
                return queues;
            }
        }
        return noOfQueues-1;
    }


}
