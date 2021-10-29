package com.example.demo.test.Controllers;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Services.JobService;
import com.example.demo.test.Services.MachineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/Machine")
public class MachineController
{
    @Autowired
    MachineService machineService;
    @Autowired
    JobService jobService;
    @Autowired
    ObjectMapper mapper;

    public MachineController()
    {
    }
    @PostMapping("/create")
    public Machine createMachine(@RequestBody Machine machine1)
    {
        if (machineService.findMachine(machine1.getMachineId()).isPresent())
        {//  his machine id is already exit with this id.
            return null ;
        }
        else
        {
            machineService.save(machine1);
            return machine1 ;
        }
    }
    @PutMapping("/update")
    public Machine updateMachine(@RequestBody Machine machine1)
    {
        if (machineService.findMachine(machine1.getMachineId()).isPresent())
        {
            machineService.save(machine1);
            return  machine1 ;
        }
        else
        {
            // this means machine does not exist ,so we can not update
            return null;
        }
    }
    @DeleteMapping("/delete_a_machine")
    public String deleteMachine(@RequestBody Machine machine)
    {
        Optional<Machine> optionalMachine = machineService.findMachine(machine.getMachineId());
        if (optionalMachine.isPresent())
        {
            Machine tempMachine = optionalMachine.get();
            jobService.deleteTasks(tempMachine);
            machineService.delete(tempMachine);
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("id", tempMachine.getMachineId());
            objectNode.put("name", tempMachine.getName());
            objectNode.put("ip", tempMachine.getIp());
            objectNode.put("location", tempMachine.getLocation());
            return objectNode.toString() ;
        }
        return "machine does not exist";
    }
}