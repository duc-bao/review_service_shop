package com.ducbao.service_be.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonMapper {
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    // Handle Update entity
    public <T> void maptoObject(Object source, T targetClass) {
         modelMapper.map(source, targetClass);
    }
    public <T> T map(Object source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public <T> T readValue(String jsonString, Class<T> destinationClass) throws JsonProcessingException {
        try {
            return objectMapper.readValue(jsonString, destinationClass);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    public <T> String writeValueAsString(T value) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    public <T> T convertValue(Object source, TypeReference<T> typeReference) throws JsonProcessingException {
        try {
            return objectMapper.convertValue(source, typeReference);
        } catch (Exception e) {
            throw e;
        }
    }
}
