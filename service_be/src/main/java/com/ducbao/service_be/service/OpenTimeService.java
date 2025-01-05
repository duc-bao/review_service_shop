package com.ducbao.service_be.service;

import com.ducbao.service_be.model.entity.OpenTimeModel;
import com.ducbao.service_be.repository.OpenTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenTimeService {
    private final OpenTimeRepository openTimeRepository;

    public OpenTimeModel getOpenTimeModel(String id) {
        if(id == null){
            return null;
        }
        return openTimeRepository.findById(id).orElse(null);
    }
}
