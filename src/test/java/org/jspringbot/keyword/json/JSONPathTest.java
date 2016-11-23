package org.jspringbot.keyword.json;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.jspringbot.spring.ApplicationContextHolder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
//@Ignore
public class JSONPathTest {
    @Autowired
    private JSONHelper helper;
    @Autowired
    private ApplicationContext applicationContext;

    private String getJson(String path) throws IOException {
        Resource resource = applicationContext.getResource(path);

        return IOUtils.toString(resource.getInputStream(), CharEncoding.UTF_8);
    }

    @Test
    public void testSample() throws Exception {
        helper.setJsonString(getJson("classpath:test.json"));

        List<Object> nowShowing = helper.getJsonValues("movies[?(@.showing == false)]");
        assertThat(nowShowing.size(), is(2));
    }

    @Test
    public void testSample2() throws Exception {
        JSONUtils.setHelper(helper);
        helper.setJsonString(getJson("classpath:movies.json"));

        List<String> keys = JSONUtils.propertyNames("Movies[1]");

        System.out.println("keys " + keys);
    }
}
