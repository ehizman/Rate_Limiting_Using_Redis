package com.ehizman.mmr_application.repositories;

import com.ehizman.mmr_application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
