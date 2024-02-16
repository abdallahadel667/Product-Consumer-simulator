package com.example.ProductConsumer;

import com.example.ProductConsumer.Controller.ProcessController;
import com.example.ProductConsumer.Controller.SystemAssembler;

public class Test {
    public static void main(String[] args) {

//        int[][] matrix = {
//                {-1, 1, 0},
//                {-1, 1,0 },
//                {0, -1,1 }
//        };
//        int[][] matrix = {
//                {-1, 1, 0, 0},
//                {-1, 0,1, 0 },
//                {0, -1, -1, 1 },
//                {0,-1,0,1}
//        };
        int[][] matrix = {
                {-1, 1, 0},
                {0, -1,1 },
                {0, -1,1 }
        };
        SystemAssembler myEncoder = SystemAssembler.getInstance();
        myEncoder.simulationInitializer(matrix,9);
        ProcessController controller= new ProcessController();
        controller.run();
        System.out.println(myEncoder.getMyMachines());
    }
    }







