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

@Service
public class LoadService {

    private final LoadRepository loadRepository;

    @Autowired
    public LoadService(LoadRepository loadRepository) {
        this.loadRepository = loadRepository;
    }

    @Transactional
    public LoadDTO createLoad(LoadDTO loadDTO) {
        Load load = new Load();

        // Copy properties of LoadDTO to Load entity
        BeanUtils.copyProperties(loadDTO, load);

        // Manually map FacilityDTO to Facility (since BeanUtils does not handle nested objects)
        Facility facility = new Facility();
        BeanUtils.copyProperties(loadDTO.getFacility(), facility);
        load.setFacility(facility);

        // Set the default status
        load.setStatus(LoadStatus.POSTED);

        Load savedLoad = loadRepository.save(load);

        // Map saved Load entity to LoadDTO
        LoadDTO savedLoadDTO = new LoadDTO();
        BeanUtils.copyProperties(savedLoad, savedLoadDTO);

        return savedLoadDTO;
    }

    public List<LoadDTO> getAllLoads() {
        return loadRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoadDTO> getLoadsByShipperId(String shipperId) {
        return loadRepository.findByShipperId(shipperId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoadDTO> getLoadsByTruckType(String truckType) {
        return loadRepository.findByTruckType(truckType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LoadDTO getLoadById(UUID loadId) {
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));
        return convertToDTO(load);
    }

    @Transactional
    public LoadDTO updateLoad(UUID loadId, LoadDTO loadDTO) {
        Load existingLoad = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));

        // Don't update id and datePosted
        loadDTO.setId(existingLoad.getId());
        loadDTO.setDatePosted(existingLoad.getDatePosted());

        // Copy properties from loadDTO to existingLoad (except for id and datePosted)
        BeanUtils.copyProperties(loadDTO, existingLoad);

        // Manually update the facility field (since BeanUtils does not handle nested objects)
        if (loadDTO.getFacility() != null) {
            Facility updatedFacility = new Facility();
            BeanUtils.copyProperties(loadDTO.getFacility(), updatedFacility);
            existingLoad.setFacility(updatedFacility);
        }

        Load updatedLoad = loadRepository.save(existingLoad);

        return convertToDTO(updatedLoad);
    }

    @Transactional
    public void deleteLoad(UUID loadId) {
        if (!loadRepository.existsById(loadId)) {
            throw new ResourceNotFoundException("Load not found with id: " + loadId);
        }
        loadRepository.deleteById(loadId);
    }

    @Transactional
    public void updateLoadStatus(UUID loadId, LoadStatus status) {
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));
        load.setStatus(status);
        loadRepository.save(load);
    }

    private LoadDTO convertToDTO(Load load) {
        LoadDTO loadDTO = new LoadDTO();
        BeanUtils.copyProperties(load, loadDTO);
        return loadDTO;
    }
}
