package com.capstone.arfly.jetson.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

// 비동기 핸들러에서 지연 로딩이 일어나지 않도록 엔티티 대신 필요한 값만 담음
@Getter
public class JetsonAlertCreatedEvent extends ApplicationEvent {
    private final Long ownerId;
    private final String diseaseName;
    private final Double probability;

    public JetsonAlertCreatedEvent(Object source, Long ownerId, String diseaseName, Double probability) {
        super(source);
        this.ownerId = ownerId;
        this.diseaseName = diseaseName;
        this.probability = probability;
    }
}
