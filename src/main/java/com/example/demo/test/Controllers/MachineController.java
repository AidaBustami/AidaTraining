package com.example.demo.test.Controllers;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Services.JobService;
import com.example.demo.test.Services.MachineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Machine/")
public class MachineController {
    @Autowired
    MachineService machineService;
    @Autowired

    JobService jobService;

    public MachineController() {
    }

    @PostMapping()
    public Machine createMachine(@RequestBody Machine machine) {
        Machine result = machineService.save(machine);
        return result;
    }

    @PutMapping("{machineId}")
    public Machine updateMachine(@PathVariable Long machineId, @RequestBody Machine machine) {
        if (machineService.findMachine(machineId).isPresent()) {
            machine.setMachineId(machineId);
            Machine result = machineService.save(machine);
            return result;
        } else {
            // this means machine does not exist ,so we can not update
            return null;
        }
    }

    @GetMapping("{machineId}")
    public Machine getMachineById(@PathVariable Long machineId) {
        Optional<Machine> machine = machineService.findById(machineId);
        if (machine.isPresent())
            return machine.get();
        else
            return null;
    }

    @GetMapping("")
    public List<Machine> getAllMachines() {
        return machineService.findAll();
    }

    @DeleteMapping("{machineId}")
    public String deleteMachine(@PathVariable Long machineId) {
        Optional<Machine> optionalMachine = machineService.findMachine(machineId);
        if (optionalMachine.isPresent()) {
            Machine tempMachine = optionalMachine.get();
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(tempMachine);
                // if we  use cascade_remove  we don't need to call jobService.deleteTasks.
                jobService.deleteTasks(tempMachine);
                machineService.delete(tempMachine.getMachineId());
                return "Machine " + json + "is deleted ";
            } catch (Exception e) {
                return e + "";
            }
        }
        return "machine does not exist";
    }
}