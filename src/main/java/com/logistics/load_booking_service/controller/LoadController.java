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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/load")
public class LoadController {

    private static final Logger logger = LoggerFactory.getLogger(LoadController.class);

    private final LoadService loadService;

    @Autowired
    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    @PostMapping
    public ResponseEntity<LoadDTO> createLoad(@Valid @RequestBody LoadDTO loadDTO) {
        logger.info("Entering createLoad with loadDTO: {}", loadDTO);
        LoadDTO createdLoad = loadService.createLoad(loadDTO);
        logger.info("Exiting createLoad with createdLoad: {}", createdLoad);
        return new ResponseEntity<>(createdLoad, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LoadDTO>> getLoads(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false) String truckType) {
        logger.info("Entering getLoads with shipperId: {}, truckType: {}", shipperId, truckType);

        List<LoadDTO> loads;

        if (shipperId != null) {
            loads = loadService.getLoadsByShipperId(shipperId);
        } else if (truckType != null) {
            loads = loadService.getLoadsByTruckType(truckType);
        } else {
            loads = loadService.getAllLoads();
        }

        logger.info("Exiting getLoads with {} loads", loads.size());
        return ResponseEntity.ok(loads);
    }

    @GetMapping("/{loadId}")
    public ResponseEntity<LoadDTO> getLoadById(@PathVariable UUID loadId) {
        logger.info("Entering getLoadById with loadId: {}", loadId);
        LoadDTO load = loadService.getLoadById(loadId);
        logger.info("Exiting getLoadById with load: {}", load);
        return ResponseEntity.ok(load);
    }

    @PutMapping("/{loadId}")
    public ResponseEntity<LoadDTO> updateLoad(
            @PathVariable UUID loadId,
            @Valid @RequestBody LoadDTO loadDTO) {
        logger.info("Entering updateLoad with loadId: {}, loadDTO: {}", loadId, loadDTO);
        LoadDTO updatedLoad = loadService.updateLoad(loadId, loadDTO);
        logger.info("Exiting updateLoad with updatedLoad: {}", updatedLoad);
        return ResponseEntity.ok(updatedLoad);
    }

    @DeleteMapping("/{loadId}")
    public ResponseEntity<Void> deleteLoad(@PathVariable UUID loadId) {
        logger.info("Entering deleteLoad with loadId: {}", loadId);
        loadService.deleteLoad(loadId);
        logger.info("Exiting deleteLoad with loadId: {}", loadId);
        return ResponseEntity.noContent().build();
    }
}
