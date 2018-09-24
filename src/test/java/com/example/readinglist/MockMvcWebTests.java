/**
 * weiguoxian
 */
package com.example.readinglist;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author weiguoxian
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReadingListApplication.class)
@WebAppConfiguration
public class MockMvcWebTests {
	
	@Autowired
	private WebApplicationContext webContext;
	private MockMvc mockMvc;
	
	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webContext)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	public void homePage() throws Exception {
		mockMvc.perform(get("/readingList/xian"))
		.andExpect(status().isOk())
		.andExpect(view().name("readingList"))
		.andExpect(model().attributeExists("books"))
		.andExpect(model().attribute("books", is(empty())));
	}
	
	@Test
	public void postBook() throws Exception{
		mockMvc.perform(post("/readingList/xian")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", "BOOK TITLE")
				.param("author", "BOOK AUTHOR")
				.param("isbn", "1234567890")
				.param("description", "DESCRIPTION"))
		.andExpect(status().is3xxRedirection())
		.andExpect(header().string("Location", "/readingList/xian"));
		
		Book expectedBook = new Book();
		expectedBook.setId(1L);
		expectedBook.setReader("xian");
		expectedBook.setTitle("BOOK TITLE");
		expectedBook.setAuthor("BOOK AUTHOR");
		expectedBook.setIsbn("1234567890");
		expectedBook.setDescription("DESCRIPTION");
		
		mockMvc.perform(get("/readingList/xian"))
			.andExpect(status().isOk())
			.andExpect(view().name("readingList"))
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", hasSize(1)))
			.andExpect(model().attribute("books", 
					contains(samePropertyValuesAs(expectedBook))));
	}
	
	@Test
	public void homePage_unauthenticatedUser() throws Exception{
		mockMvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "http://localhost/login"));
	}
	
	@Test
	@WithMockUser(username="craig",
				  password="password",
				  roles="READER")
	public void homePage_authenticatedUser() throws Exception{
		Reader expectedReader = new Reader();
		expectedReader.setUsername("craig");
		expectedReader.setPassword("password");
		expectedReader.setFullname("Craig Walls");
		
		mockMvc.perform(get("/readingList/craig"))
			.andExpect(status().isOk())
			.andExpect(view().name("readingList"))
//			.andExpect(model().attribute("reader", 
//							   samePropertyValuesAs(expectedReader)))
			.andExpect(model().attribute("books", hasSize(0)));
	}
	
}
