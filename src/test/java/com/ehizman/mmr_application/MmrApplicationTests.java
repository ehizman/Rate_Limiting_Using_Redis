package com.ehizman.mmr_application;

import com.ehizman.mmr_application.models.PhoneNumber;
import com.ehizman.mmr_application.repositories.PhoneNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
class MmrApplicationTests {
   @Autowired
   DataSource dataSource;
   @Autowired
    PhoneNumberRepository phoneNumberRepository;

    @Test
    public void connectToDatabaseTest() {
        assertThat(dataSource).isNotNull();
        log.info("Datasource properties ->{}", dataSource);
        try {
            Connection connection = dataSource.getConnection();
            assertThat(connection).isNotNull();
            PhoneNumber phoneNumber = phoneNumberRepository.findById(1).orElse(null);
            assertNotNull(phoneNumber);
            log.info("Phone number --> {}", phoneNumber);
            log.info("Database -> {}", connection.getSchema());
        } catch (SQLException ex) {
            log.info("An exception occurred -> {}", ex.getMessage());
        }
    }

}
