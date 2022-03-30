package com.ehizman.mmr_application.repositories;

import com.ehizman.mmr_application.models.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
}
