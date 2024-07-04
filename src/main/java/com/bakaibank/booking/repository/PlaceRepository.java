package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
