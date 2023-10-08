package org.juke.remix.service;

import com.example.Greeting;
import com.example.GreetingController;
import com.example.GreetingServiceImpl;
import com.example.IGreetingController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.juke.framework.dao.ZipUtil;
import org.juke.framework.exception.JukeAccessException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(properties={"juke.path=C:/temp","juke.zip=juketest2","juke.tests=juketest,juketest2","juke=replay"})
@TestPropertySource(properties={"juke.path=C:/temp","juke.zip=juketest2","juke.tests=juketest,juketest2","juke=replay"})
@ContextConfiguration(classes = {RemixServiceImpl.class, GreetingController.class, GreetingServiceImpl.class})
class TestingWebAppTests {
    @Autowired
    RemixService remixService;
    @Autowired
    IGreetingController greetingController;
    static boolean isLoaded=false;
    private static Logger log = LoggerFactory.getLogger(TestingWebAppTests.class);

static {
    System.setProperty("juke.path", System.getProperty("java.io.tmpdir"));
    System.setProperty("juke.zip","juketest2");
    System.setProperty("juke.tests","juketest,juketest2");
    new java.io.File(System.getProperty("java.io.tmpdir"),"juketest2").delete();

    ZipUtil zip=new ZipUtil(System.getProperty("java.io.tmpdir"), "juketest2");
    try {
        zip.createZipFile(System.getProperty("java.io.tmpdir")+"/juketest2.zip",TestData.map);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    System.setProperty("juke","replay");
}

@BeforeAll
void buildTestFile() throws IOException {


}


    @Test
    void contextLoads() {
        assert(remixService != null);
        assert(greetingController != null);
    }

    @Test
    void testGreeting() throws JukeAccessException {
        log.debug("isLoaded: " + isLoaded);

        Greeting greeting  = greetingController.greeting("World");
        assertEquals (greeting.getContent(),"Hello, test!");
        greeting = greetingController.greeting("World");
        assertEquals (greeting.getContent(),"Hello, World!");
        greeting = greetingController.greeting("World");
        assertEquals (greeting.getContent(),"Hello, cruel world!");


    }
}
