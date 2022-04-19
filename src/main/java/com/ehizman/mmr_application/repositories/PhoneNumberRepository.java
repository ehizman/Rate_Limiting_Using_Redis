package com.ehizman.mmr_application.repositories;

import com.ehizman.mmr_application.models.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
    Optional<PhoneNumber> findByNumber(String number);
}
