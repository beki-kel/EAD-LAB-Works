package com.alenedaj.repository;

import com.alenedaj.model.GasStation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GasStationRepository extends MongoRepository<GasStation, ObjectId> {
    List<GasStation> findByFuelAvailableTrue();

    Optional<Object> findByName(String name);
}
