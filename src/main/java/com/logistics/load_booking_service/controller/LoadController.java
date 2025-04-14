package com.logistics.load_booking_service.controller;

import com.logistics.load_booking_service.dto.LoadDTO;
import com.logistics.load_booking_service.service.LoadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/load")
public class LoadController {

    private final LoadService loadService;

    @Autowired
    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    @PostMapping
    public ResponseEntity<LoadDTO> createLoad(@Valid @RequestBody LoadDTO loadDTO) {
        System.out.println("Received LoadDTO: " + loadDTO);
        LoadDTO createdLoad = loadService.createLoad(loadDTO);
        return new ResponseEntity<>(createdLoad, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LoadDTO>> getLoads(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false) String truckType) {

        List<LoadDTO> loads;

        if (shipperId != null) {
            loads = loadService.getLoadsByShipperId(shipperId);
        } else if (truckType != null) {
            loads = loadService.getLoadsByTruckType(truckType);
        } else {
            loads = loadService.getAllLoads();
        }

        return ResponseEntity.ok(loads);
    }

    @GetMapping("/{loadId}")
    public ResponseEntity<LoadDTO> getLoadById(@PathVariable UUID loadId) {
        LoadDTO load = loadService.getLoadById(loadId);
        return ResponseEntity.ok(load);
    }

    @PutMapping("/{loadId}")
    public ResponseEntity<LoadDTO> updateLoad(
            @PathVariable UUID loadId,
            @Valid @RequestBody LoadDTO loadDTO) {

        LoadDTO updatedLoad = loadService.updateLoad(loadId, loadDTO);
        return ResponseEntity.ok(updatedLoad);
    }

    @DeleteMapping("/{loadId}")
    public ResponseEntity<Void> deleteLoad(@PathVariable UUID loadId) {
        loadService.deleteLoad(loadId);
        return ResponseEntity.noContent().build();
    }
}