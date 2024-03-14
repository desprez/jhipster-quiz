package com.github.desprez.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

public class CustomResponseHandler<T> implements ResultHandler {

    private final Logger log = LoggerFactory.getLogger(QuizzResource.class);

    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private final Class<? extends Collection> collectionClass;
    private T responseObject;
    private String responseData;
    private final Class<T> type;
    private Map<String, String> headers;
    private String contentType;

    public CustomResponseHandler() {
        this.type = null;
        this.collectionClass = null;
    }

    public CustomResponseHandler(Class<T> type) {
        this.type = type;
        this.collectionClass = null;
    }

    public CustomResponseHandler(Class type, Class<? extends Collection> collectionClass) {
        this.type = type;
        this.collectionClass = collectionClass;
    }

    protected <T> T responseToObject(MockHttpServletResponse response, Class<T> type) throws IOException {
        String json = getResponseAsContentsAsString(response);
        if (org.apache.commons.lang3.StringUtils.isEmpty(json)) {
            return null;
        }
        return objectMapper.readValue(json, type);
    }

    protected <T> T responseToObjectCollection(
        MockHttpServletResponse response,
        Class<? extends Collection> collectionType,
        Class<T> collectionContents
    ) throws IOException {
        String json = getResponseAsContentsAsString(response);
        if (org.apache.commons.lang3.StringUtils.isEmpty(json)) {
            return null;
        }

        JavaType type = objectMapper.getTypeFactory().constructCollectionType(collectionType, collectionContents);
        return objectMapper.readValue(json, type);
    }

    protected String getResponseAsContentsAsString(MockHttpServletResponse response) throws IOException {
        String content = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getContentAsByteArray())));
        String line;
        while ((line = br.readLine()) != null) content += line;
        br.close();

        return content;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        if (type != null) {
            if (collectionClass != null) {
                responseObject = responseToObjectCollection(result.getResponse(), collectionClass, type);
            } else {
                responseObject = responseToObject(result.getResponse(), type);
            }
        } else {
            responseData = getResponseAsContentsAsString(result.getResponse());
        }

        headers = getHeaders(result);
        contentType = result.getResponse().getContentType();

        if (result.getResolvedException() != null) {
            log.error("Exception: {}", result.getResponse().getErrorMessage());
            log.error("Error: {}", result.getResolvedException());
        }
    }

    private Map<String, String> getHeaders(MvcResult result) {
        Map<String, String> headers = new HashMap<>();
        result.getResponse().getHeaderNames().forEach(header -> headers.put(header, result.getResponse().getHeader(header)));
        return headers;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getContentType() {
        return contentType;
    }

    public T getResponseObject() {
        return responseObject;
    }
}
