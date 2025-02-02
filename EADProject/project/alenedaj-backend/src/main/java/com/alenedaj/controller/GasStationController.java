package com.alenedaj.controller;

import com.alenedaj.dto.GasStationDTO;
import com.alenedaj.service.GasStationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GasStationController {

    private static final Logger logger = LoggerFactory.getLogger(GasStationController.class);

    private final GasStationService gasStationService;

    public GasStationController(GasStationService gasStationService) {
        this.gasStationService = gasStationService;
    }

    // Admin: Add a new gas station (Uses location name)
    @PostMapping("/admin/gas-stations")
    public ResponseEntity<GasStationDTO> addGasStation(@Valid @RequestBody GasStationDTO gasStationDTO) {
        logger.info("Adding gas station {}", gasStationDTO.getName());
        GasStationDTO result = gasStationService.addGasStation(gasStationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Admin: Update gas station (Allows updating location)
    @PutMapping("/admin/gas-stations/{id}")
    public ResponseEntity<GasStationDTO> updateGasStation(@PathVariable String id, @Valid @RequestBody GasStationDTO gasStationDTO) {
        logger.info("Updating gas station with id {}", id);
        GasStationDTO result = gasStationService.updateGasStation(id, gasStationDTO);
        return ResponseEntity.ok(result);
    }

    // Admin: Delete gas station
    @DeleteMapping("/admin/gas-stations/{id}")
    public ResponseEntity<String> deleteGasStation(@PathVariable String id) {
        logger.info("Deleting gas station with id {}", id);
        gasStationService.deleteGasStation(id);
        return ResponseEntity.ok("Gas station deleted");
    }

    // User: Get distance to a gas station
    @GetMapping("/gas-stations/distance/{userId}/{stationId}")
    public ResponseEntity<Double> getDistanceToStation(@PathVariable String userId,
                                                       @PathVariable String stationId) {
        logger.info("Getting distance from user {} to station {}", userId, stationId);
        double distance = gasStationService.getDistanceToStation(userId, stationId);
        return ResponseEntity.ok(distance);
    }


    // User: Update traffic level manually via the frontend GUI
    @PutMapping("/gas-stations/update-traffic/{id}")
    public ResponseEntity<GasStationDTO> updateTrafficLevel(@PathVariable String id,
                                                            @RequestBody GasStationDTO gasStationDTO) {
        logger.info("Updating traffic level for station {}: {}", id, gasStationDTO.getTrafficLevel());
        GasStationDTO updated = gasStationService.updateTrafficLevel(id, gasStationDTO.getTrafficLevel());
        return ResponseEntity.ok(updated);
    }

    // Get all gas stations
    @GetMapping("/gas-stations")
    public ResponseEntity<List<GasStationDTO>> getAllGasStations() {
        logger.info("Fetching all gas stations");
        List<GasStationDTO> stations = gasStationService.getAllGasStations();
        return ResponseEntity.ok(stations);
    }
}
