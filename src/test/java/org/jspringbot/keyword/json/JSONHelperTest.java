package org.jspringbot.keyword.json;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test.xml" })
public class JSONHelperTest {
	@Autowired
	private JSONHelper helper;
	@Autowired
	private ApplicationContext applicationContext;

	private String getJson(String path) throws IOException {
		Resource resource = applicationContext.getResource(path);

		return IOUtils.toString(resource.getInputStream(), CharEncoding.UTF_8);
	}

	@Test
	public void testSimplePath() throws Exception {

		helper.setJsonString(getJson("classpath:store.json"));

		Object jsonValue = helper.getJsonValue("expensive");

		assertEquals(jsonValue, 10);
	}

	@Test
	public void testMultiSegmentPath() throws Exception {

		helper.setJsonString(getJson("classpath:store.json"));

		Object jsonValue = helper.getJsonValue("store.bicycle.color");

		assertEquals(jsonValue, "red");
	}

	@Test
	public void testArrayPath() throws Exception {

		helper.setJsonString(getJson("classpath:store.json"));

		Object jsonValue = helper.getJsonValue("store.book[0].title");

		assertEquals(jsonValue, "Sayings of the Century");
	}

	@Test
	public void testDottedPath() throws Exception {

		helper.setJsonString(getJson("classpath:store.json"));

		Object jsonValue = helper.getJsonValue("store.['keyword.with.dot'].working");

		assertEquals(jsonValue, "yes");
	}
	
	@Test
	public void testMultipleValuesPath() throws Exception {

		helper.setJsonString(getJson("classpath:store.json"));

		List<Object> jsonValues = helper.getJsonValues("store.book[*]");

		assertEquals(jsonValues.size(), 4);
		
		assertEquals(jsonValues.get(0).toString(), "{category=reference, author=Nigel Rees, title=Sayings of the Century, price=8.95}");
		assertEquals(jsonValues.get(1).toString(), "{category=fiction, author=Evelyn Waugh, title=Sword of Honour, price=12.99}");
		assertEquals(jsonValues.get(2).toString(), "{category=fiction, author=Herman Melville, title=Moby Dick, isbn=0-553-21311-3, price=8.99}");
		assertEquals(jsonValues.get(3).toString(), "{category=fiction, author=J. R. R. Tolkien, title=The Lord of the Rings, isbn=0-395-19395-8, price=22.99}");
				
	}
}
