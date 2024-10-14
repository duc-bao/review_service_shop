package com.ducbao.service_be.service;

import com.ducbao.service_be.model.constant.AppConstants;
import com.ducbao.service_be.model.dto.request.EmailRequest;
import com.ducbao.service_be.model.dto.response.EmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Value("${email.api.url}")
    private String apiUrl;
    @Value("${email.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public void sendEmail(EmailRequest emailRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("api-key", apiKey);
        Map<String, Object> bodyMap  = new HashMap<>();
        bodyMap.put("sender", new HashMap<String, String>(){
            {
                put("name", "Service Review");
                put("email", "truongducbao290402@gmail.com");
            }
        });
        bodyMap.put("subject", emailRequest.getSubject());
        bodyMap.put("to", new HashMap[] {
                new HashMap<String, String>() {{
                    put("email", emailRequest.getRecipient());
                    put("name", (String) emailRequest.getParam().getOrDefault("name", ""));
                }}
        });
        String content = emailRequest.getBody();
        log.info("Email params: {}", emailRequest.getParam());
        if (emailRequest.getTemplateCode() != null && emailRequest.getTemplateCode().equals("REGISTER")) {
            content = AppConstants.CONTENT_REGISTER
                    .replace("[[name]]", (String) emailRequest.getParam().getOrDefault("name", ""))
                    .replace("[[URL]]", (String) emailRequest.getParam().getOrDefault("verificationUrl", ""));
        }
        if(emailRequest.getTemplateCode() != null && emailRequest.getTemplateCode().equals("ACTIVE")) {
            content = AppConstants.CONTENT_SHOP_ACTIVATION
                    .replace("[[name]]", (String) emailRequest.getParam().getOrDefault("name", ""))
                    .replace("[[URL]]", (String) emailRequest.getParam().getOrDefault("verificationUrl", ""));
        }
        bodyMap.put("htmlContent", content);
        log.info("Sending email with content: {}", content);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(bodyMap, headers);

        try {
          restTemplate.postForEntity(apiUrl, request, EmailResponse.class);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
