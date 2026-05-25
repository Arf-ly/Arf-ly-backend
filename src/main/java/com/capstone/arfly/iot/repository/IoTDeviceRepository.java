package com.capstone.arfly.iot.repository;

import com.capstone.arfly.iot.domain.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {

    Optional<IoTDevice> findByDeviceUid(String deviceUid);
}
