package com.cma.systemc.service;

import com.cma.systemc.entity.IntegrationLog;
import com.cma.systemc.repository.IntegrationLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntegrationLogService {
    private final IntegrationLogRepository repository;
    private final ObjectMapper objectMapper;

    public IntegrationLogService(IntegrationLogRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void log(String sourceSystem, String messageType, String referenceNo, Object request, Object response, String status) {
        IntegrationLog log = new IntegrationLog();
        log.setSourceSystem(sourceSystem);
        log.setMessageType(messageType);
        log.setReferenceNo(referenceNo);
        log.setRequestPayload(toJson(request));
        log.setResponsePayload(toJson(response));
        log.setStatus(status);
        repository.save(log);
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return String.valueOf(value);
        }
    }
}
