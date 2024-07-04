package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingTypeRepository extends JpaRepository<BookingType, Long> {
}
