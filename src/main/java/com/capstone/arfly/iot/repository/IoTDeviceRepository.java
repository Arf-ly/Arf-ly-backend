package com.capstone.arfly.iot.repository;

import com.capstone.arfly.iot.domain.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // 🌟 핵심 포인트!

public interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {

    /**
     * 라즈베리파이 피코가 보내준 deviceUid로 등록된 기기를 찾습니다.
     * Optional로 감싸서 반환해야 Service 코드에서 예외 처리(.orElseThrow)를 우아하게 할 수 있습니다.
     */
    Optional<IoTDevice> findByDeviceUid(String deviceUid);

}