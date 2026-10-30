package com.capstone.arfly.jetson.repository;

import com.capstone.arfly.jetson.domain.JetsonDevice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JetsonDeviceRepository extends JpaRepository<JetsonDevice, Long> {
    Optional<JetsonDevice> findByTokenHash(String tokenHash);
}
