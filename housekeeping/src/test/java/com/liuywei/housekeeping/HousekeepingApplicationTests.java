package com.liuywei.housekeeping;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@SpringBootTest
class HousekeepingApplicationTests {

    @Test
    void test() {
        BigDecimal a = new BigDecimal(250);
        System.out.println(a.multiply(new BigDecimal(0.05)).setScale(4,BigDecimal.ROUND_HALF_UP));
    }


}
