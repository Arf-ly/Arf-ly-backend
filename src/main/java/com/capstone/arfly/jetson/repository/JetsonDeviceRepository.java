package com.capstone.arfly.jetson.repository;

import com.capstone.arfly.jetson.domain.JetsonDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JetsonDeviceRepository extends JpaRepository<JetsonDevice, Long> {}
