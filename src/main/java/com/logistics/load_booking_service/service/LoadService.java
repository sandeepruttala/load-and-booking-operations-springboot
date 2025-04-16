package com.logistics.load_booking_service.service;

import com.logistics.load_booking_service.dto.LoadDTO;
import com.logistics.load_booking_service.exception.ResourceNotFoundException;
import com.logistics.load_booking_service.model.Facility;
import com.logistics.load_booking_service.model.Load;
import com.logistics.load_booking_service.model.LoadStatus;
import com.logistics.load_booking_service.repository.LoadRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoadService {

    private static final Logger logger = LoggerFactory.getLogger(LoadService.class);

    private final LoadRepository loadRepository;

    @Autowired
    public LoadService(LoadRepository loadRepository) {
        this.loadRepository = loadRepository;
    }

    @Transactional
    public LoadDTO createLoad(LoadDTO loadDTO) {
        logger.info("Entering createLoad with loadDTO: {}", loadDTO);
        Load load = new Load();
        BeanUtils.copyProperties(loadDTO, load);

        Facility facility = new Facility();
        BeanUtils.copyProperties(loadDTO.getFacility(), facility);
        load.setFacility(facility);

        load.setStatus(LoadStatus.POSTED);

        Load savedLoad = loadRepository.save(load);

        LoadDTO savedLoadDTO = new LoadDTO();
        BeanUtils.copyProperties(savedLoad, savedLoadDTO);
        logger.info("Exiting createLoad with savedLoadDTO: {}", savedLoadDTO);
        return savedLoadDTO;
    }

    public List<LoadDTO> getAllLoads() {
        logger.info("Entering getAllLoads");
        List<LoadDTO> loads = loadRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Exiting getAllLoads with {} loads", loads.size());
        return loads;
    }

    public List<LoadDTO> getLoadsByShipperId(String shipperId) {
        logger.info("Entering getLoadsByShipperId with shipperId: {}", shipperId);
        List<LoadDTO> loads = loadRepository.findByShipperId(shipperId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Exiting getLoadsByShipperId with {} loads", loads.size());
        return loads;
    }

    public List<LoadDTO> getLoadsByTruckType(String truckType) {
        logger.info("Entering getLoadsByTruckType with truckType: {}", truckType);
        List<LoadDTO> loads = loadRepository.findByTruckType(truckType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Exiting getLoadsByTruckType with {} loads", loads.size());
        return loads;
    }

    public LoadDTO getLoadById(UUID loadId) {
        logger.info("Entering getLoadById with loadId: {}", loadId);
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> {
                    logger.error("Load not found with id: {}", loadId);
                    return new ResourceNotFoundException("Load not found with id: " + loadId);
                });
        LoadDTO loadDTO = convertToDTO(load);
        logger.info("Exiting getLoadById with loadDTO: {}", loadDTO);
        return loadDTO;
    }

    @Transactional
    public LoadDTO updateLoad(UUID loadId, LoadDTO loadDTO) {
        logger.info("Entering updateLoad with loadId: {}, loadDTO: {}", loadId, loadDTO);
        Load existingLoad = loadRepository.findById(loadId)
                .orElseThrow(() -> {
                    logger.error("Load not found with id: {}", loadId);
                    return new ResourceNotFoundException("Load not found with id: " + loadId);
                });

        loadDTO.setId(existingLoad.getId());
        loadDTO.setDatePosted(existingLoad.getDatePosted());

        BeanUtils.copyProperties(loadDTO, existingLoad);

        if (loadDTO.getFacility() != null) {
            Facility updatedFacility = new Facility();
            BeanUtils.copyProperties(loadDTO.getFacility(), updatedFacility);
            existingLoad.setFacility(updatedFacility);
        }

        Load updatedLoad = loadRepository.save(existingLoad);
        LoadDTO updatedLoadDTO = convertToDTO(updatedLoad);
        logger.info("Exiting updateLoad with updatedLoadDTO: {}", updatedLoadDTO);
        return updatedLoadDTO;
    }

    @Transactional
    public void deleteLoad(UUID loadId) {
        logger.info("Entering deleteLoad with loadId: {}", loadId);
        if (!loadRepository.existsById(loadId)) {
            logger.error("Load not found with id: {}", loadId);
            throw new ResourceNotFoundException("Load not found with id: " + loadId);
        }
        loadRepository.deleteById(loadId);
        logger.info("Exiting deleteLoad with loadId: {}", loadId);
    }

    @Transactional
    public void updateLoadStatus(UUID loadId, LoadStatus status) {
        logger.info("Entering updateLoadStatus with loadId: {}, status: {}", loadId, status);
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> {
                    logger.error("Load not found with id: {}", loadId);
                    return new ResourceNotFoundException("Load not found with id: " + loadId);
                });
        load.setStatus(status);
        loadRepository.save(load);
        logger.info("Exiting updateLoadStatus with loadId: {}, status: {}", loadId, status);
    }

    private LoadDTO convertToDTO(Load load) {
        LoadDTO loadDTO = new LoadDTO();
        BeanUtils.copyProperties(load, loadDTO);
        return loadDTO;
    }
}
