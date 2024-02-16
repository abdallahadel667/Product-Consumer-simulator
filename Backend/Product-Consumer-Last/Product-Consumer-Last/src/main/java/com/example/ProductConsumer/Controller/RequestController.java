package com.example.ProductConsumer.Controller;

import com.example.ProductConsumer.DTOs.RequestDTO;
import com.example.ProductConsumer.DTOs.ResponseDTO;
import com.example.ProductConsumer.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.Queue;

@RestController
@CrossOrigin
public class RequestController {

    private final ProcessController processController;
    private final SystemAssembler assembler;

    @Autowired
    public RequestController() {
        this.processController = ProcessController.getInstance();
        this.assembler = SystemAssembler.getInstance();
    }

    @PostMapping("/construct/{inputs}")
    public String construct(@RequestBody RequestDTO network, @PathVariable int inputs) {
        assembler.simulationInitializer(network.getArchitecture(), inputs);
        processController.run();
        return "success";
    }

    @GetMapping("/live")
    public LinkedList<ResponseDTO> liveUpdate() {
        return processController.update();
    }

    @GetMapping("/replay")
    public void replay() {

                processController.replay();
        System.out.println(assembler.getMyQueues().getLast().size());
                 processController.run();
    }

    @DeleteMapping("/clear")
    public void clear() {
        assembler.setMyQueues(new LinkedList<>());
        assembler.setMyMachines(new LinkedList<>());
        assembler.setArchitecture(null);
        assembler.setMyProducts(new LinkedList<>());
    }
}

