package com.projekt.kiosk;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log
public class KioskApplication {
    public static void main(String[] args) {
        SpringApplication.run(KioskApplication.class, args);
    }

}
