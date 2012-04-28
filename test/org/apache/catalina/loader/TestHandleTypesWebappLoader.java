package org.apache.catalina.loader;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.TomcatBaseTest;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;

public class TestHandleTypesWebappLoader extends TomcatBaseTest {
    public static final String TEST_DIR = new File("test/webapp-3.0-spring").getAbsolutePath();

    @Test
    public void testStartInternalForLibrary() throws Exception {
        Tomcat tomcat = getTomcatInstance();
        StandardContext context = (StandardContext)tomcat.addWebapp(null, "/spring", TEST_DIR + "/src/main/webapp");
        context.setLoader(new WebappLoader());
        context.setUnpackWAR(false);

        // Add application classes as JAR file
        context.getLoader().addRepository(new File(TEST_DIR, "/target/lib/app.jar").toURI().toURL().toString());

        addLibsToRepository(context);
        tomcat.start();
    }

    @Test
    public void testStartInternalForClassesDirectory() throws Exception {
        Tomcat tomcat = getTomcatInstance();
        StandardContext context = (StandardContext)tomcat.addWebapp(null, "/spring", TEST_DIR + "/src/main/webapp");
        context.setLoader(new WebappLoader());
        context.setUnpackWAR(false);

        // Add application classes as directory
        context.getLoader().addRepository(new File(TEST_DIR, "/target/classes/main").toURI().toURL().toString());
        context.getLoader().addRepository(new File(TEST_DIR, "/src/main/resources").toURI().toURL().toString());

        addLibsToRepository(context);
        tomcat.start();
    }

    private void addLibsToRepository(StandardContext context) throws MalformedURLException {
        File folder = new File(TEST_DIR, "/lib");
        File[] libs = folder.listFiles();

        for(File lib : libs) {
            context.getLoader().addRepository(lib.toURI().toURL().toString());
        }
    }
}
