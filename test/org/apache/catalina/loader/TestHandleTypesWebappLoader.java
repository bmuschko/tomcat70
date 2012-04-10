package org.apache.catalina.loader;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.TomcatBaseTest;
import org.junit.Test;

import java.io.File;

public class TestHandleTypesWebappLoader extends TomcatBaseTest {
    public static final String TEST_DIR = "test/webapp-3.0-spring";

    @Test
    public void testStartInternal() throws Exception {
        Tomcat tomcat = getTomcatInstance();
        StandardContext context = (StandardContext)tomcat.addWebapp(null, "/spring", TEST_DIR + "/src/main/webapp");
        context.setLoader(new WebappLoader());
        context.setUnpackWAR(false);
        context.getLoader().addRepository(new File(TEST_DIR, "/src/main/java").toURI().toURL().toString());
        context.getLoader().addRepository(new File(TEST_DIR, "/src/main/resources").toURI().toURL().toString());

        File folder = new File(TEST_DIR, "/lib");
        File[] libs = folder.listFiles();

        for(File lib : libs) {
            context.getLoader().addRepository(lib.toURI().toURL().toString());
        }

        tomcat.start();
    }
}
