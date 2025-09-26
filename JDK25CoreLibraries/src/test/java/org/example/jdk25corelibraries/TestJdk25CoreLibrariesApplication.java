package org.example.jdk25corelibraries;

import org.springframework.boot.SpringApplication;

public class TestJdk25CoreLibrariesApplication {

    public static void main(String[] args) {
        SpringApplication.from(Jdk25CoreLibrariesApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
