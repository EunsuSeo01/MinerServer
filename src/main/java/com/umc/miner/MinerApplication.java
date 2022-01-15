package com.umc.miner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MinerApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml," // application.yml 에 붙이겠다.
            + "C:/Users/user/config.yml"; // 로컬에 있는 이 파일을

    public static void main(String[] args) {

        //빌더패턴
        new SpringApplicationBuilder(MinerApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);

        // 메모리 사용량 출력
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
    }
}
