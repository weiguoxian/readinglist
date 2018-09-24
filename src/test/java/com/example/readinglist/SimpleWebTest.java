/**
 * xian
 */
package com.example.readinglist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
/**
 * @author Dell 
 * not fixed 9/24
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ReadingListApplication.class)
public class SimpleWebTest {
	
	@Test(expected=HttpClientErrorException.class)
	public void pageNotFound() {
		try {
			RestTemplate rest = new RestTemplate();
			rest.getForObject("http://localhost:8080/bogusPage", String.class);
			fail("Should result in HTTP 404");
		}catch(HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
			throw e;
		}
	}
}
