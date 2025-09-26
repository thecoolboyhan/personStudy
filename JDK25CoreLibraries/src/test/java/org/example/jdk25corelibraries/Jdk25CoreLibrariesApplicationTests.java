package org.example.jdk25corelibraries;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Jdk25CoreLibrariesApplicationTests {

    @Test
    void contextLoads() {

    }

}
