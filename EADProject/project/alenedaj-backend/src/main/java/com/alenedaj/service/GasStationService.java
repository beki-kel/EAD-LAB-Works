package com.alenedaj.service;

import org.bson.types.ObjectId;
import com.alenedaj.dto.GasStationDTO;
import com.alenedaj.model.GasStation;
import com.alenedaj.model.User;
import com.alenedaj.repository.UserRepository;
import com.alenedaj.repository.GasStationRepository;
import com.alenedaj.utils.MapboxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GasStationService {
    @Autowired
    private GasStationRepository gasStationRepository;

    @Autowired
    private MapboxUtil mapboxUtil;

    @Autowired
    private UserRepository userRepository;

    // Convert DTO to Entity
    private GasStation mapToEntity(GasStationDTO dto) {
        GasStation gasStation = new GasStation();
        gasStation.setName(dto.getName());

        if (dto.getLocation() != null && !dto.getLocation().isEmpty()) {
            double[] coordinates = mapboxUtil.getCoordinates(dto.getLocation());
            gasStation.setLatitude(coordinates[0]);
            gasStation.setLongitude(coordinates[1]);
        }

        gasStation.setFuelAvailable(dto.isFuelAvailable());
        gasStation.setTrafficLevel(dto.getTrafficLevel() != null ? dto.getTrafficLevel() : "UNDEFINED");
        return gasStation;
    }

    // Convert Entity to DTO
    private GasStationDTO mapToDTO(GasStation gasStation) {
        GasStationDTO dto = new GasStationDTO();
        dto.setName(gasStation.getName());
        dto.setLatitude(gasStation.getLatitude());
        dto.setLongitude(gasStation.getLongitude());
        dto.setFuelAvailable(gasStation.isFuelAvailable());
        dto.setTrafficLevel(gasStation.getTrafficLevel());
        return dto;
    }

    // ✅ Add Gas Station
    public GasStationDTO addGasStation(GasStationDTO gasStationDTO) {
        if (gasStationRepository.findByName(gasStationDTO.getName()).isPresent()) {
            throw new RuntimeException("A gas station with this name already exists!");
        }

        double[] coordinates = mapboxUtil.getCoordinates(gasStationDTO.getLocation());
        gasStationDTO.setLatitude(coordinates[0]);
        gasStationDTO.setLongitude(coordinates[1]);

        GasStation savedStation = gasStationRepository.save(mapToEntity(gasStationDTO));
        return mapToDTO(savedStation);
    }

    // ✅ Update Gas Station (Uses ObjectId)
    public GasStationDTO updateGasStation(String id, GasStationDTO updatedStation) {
        GasStation station = gasStationRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Gas station not found"));

        station.setName(updatedStation.getName());

        if (updatedStation.getLocation() != null && !updatedStation.getLocation().isEmpty()) {
            double[] coordinates = mapboxUtil.getCoordinates(updatedStation.getLocation());
            station.setLatitude(coordinates[0]);
            station.setLongitude(coordinates[1]);
        }

        station.setFuelAvailable(updatedStation.isFuelAvailable());
        gasStationRepository.save(station);
        return mapToDTO(station);
    }

    // ✅ Delete Gas Station (Uses ObjectId)
    public void deleteGasStation(String id) {
        if (!gasStationRepository.existsById(new ObjectId(id))) {
            throw new RuntimeException("Gas station not found");
        }
        gasStationRepository.deleteById(new ObjectId(id));
    }

    // ✅ Get Distance to Gas Station
    public double getDistanceToStation(String userId, String stationId) {
        User user = userRepository.findById(new ObjectId(userId))  // ✅ Convert ID to ObjectId
                .orElseThrow(() -> new RuntimeException("User not found"));

        GasStation station = gasStationRepository.findById(new ObjectId(stationId))  // ✅ Convert ID to ObjectId
                .orElseThrow(() -> new RuntimeException("Gas station not found"));

        return mapboxUtil.calculateDistance(
                new double[]{user.getLatitude(), user.getLongitude()},
                new double[]{station.getLatitude(), station.getLongitude()}
        );
    }

    // ✅ Get Driving Directions
    public String getDirectionsToStation(String stationId, double userLat, double userLon) {
        GasStation station = gasStationRepository.findById(new ObjectId(stationId))
                .orElseThrow(() -> new RuntimeException("Gas station not found"));

        return mapboxUtil.getDirections(
                userLon + "," + userLat,
                station.getLongitude() + "," + station.getLatitude()
        );
    }

    // ✅ Update Traffic Level
    public GasStationDTO updateTrafficLevel(String id, String trafficLevel) {
        GasStation station = gasStationRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Gas station not found"));

        station.setTrafficLevel(trafficLevel);
        gasStationRepository.save(station);
        return mapToDTO(station);
    }

    // ✅ Get All Gas Stations
    public List<GasStationDTO> getAllGasStations() {
        return gasStationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
