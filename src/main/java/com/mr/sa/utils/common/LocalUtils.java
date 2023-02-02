package com.mr.sa.utils.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mr.sa.utils.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class LocalUtils {
    static private RestTemplate restTemplate = new RestTemplate();

    static private ObjectMapper mapper = new ObjectMapper();

    static public Coordinate getCoordinateByLocalAddress(String address) {

        return null;
    }
}
