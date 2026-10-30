package com.capstone.arfly.jetson.repository;

import com.capstone.arfly.jetson.domain.JetsonAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JetsonAlertRepository extends JpaRepository<JetsonAlert, Long> {}
