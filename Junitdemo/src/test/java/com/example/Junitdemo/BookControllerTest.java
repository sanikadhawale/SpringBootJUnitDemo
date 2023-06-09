package com.example.Junitdemo;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;
import lombok.Builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectWriter;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {
	
	private MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();
	
	
	
	@Mock  //Creating a mock object of Interface BookRepository
	private BookRepository bookRepository;
	
	@InjectMocks //Injecting the object into the BookController
	private BookController bookController;
	
	Book record1 = new Book("A", "B", 4);
	Book record2 = new Book("C", "D", 5);
	Book record3 = new Book("E", "F", 3);
	
	
	@Before(value = "")
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}
	
	
	@Test  //test to get all records
	public void getAllRecords_success() throws Exception {
		
		List<Book> records = new ArrayList<>();
		records.add(record1);
		records.add(record2);
		records.add(record3);
		
		//important line!!
		Mockito.when(bookRepository.findAll()).thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/book/books")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[2].name", is("E")));
	}
	
	@Test  //test to get record by id
	public void getBookById_success() throws Exception {
	
		Mockito.when(bookRepository.findById(record1.getBookId())).thenReturn(java.util.Optional.of(record1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/book/books/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("A")));
		
	}
	
	@Test    //test to check if new record added correctly
	public void createRecord_success() throws Exception {
		Book record = Book.builder()
				.bookId(4)
				.name("G")
				.summary("H")
				.rating(5)
				.build();
		
		Mockito.when(bookRepository.save(record)).thenReturn(record);
		
		String content = objectWriter.writeValueAsString(record);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content);
		
		mockMvc.perform(mockRequest)
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.name", is("G")));
		
		
		
	}
	
	//Test to check if update happening correctly
	@Test
	public void updateBookRecord_success() throws Exception {
		Book updatedRecord = Book.builder()
				.bookId(1)
				.name("Updated Name")
				.summary("Updated Summary")
				.rating(2)
				.build();
		
		Mockito.when(bookRepository.findById(record1.getBookId())).thenReturn(java.util.Optional.ofNullable(record1));
		Mockito.when(bookRepository.save(updatedRecord)).thenReturn(updatedRecord);
		
		String updatedContent = objectWriter.writeValueAsString(updatedRecord);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(updatedContent);
		
		mockMvc.perform(mockRequest)
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.name", is("Updated Name")));
		
		
		
	}
	
	@Test  //Test to check if deletion is correctly happening
	public void deleteBookById_success() throws Exception {
		
		Mockito.when(bookRepository.findById(record2.getBookId())).thenReturn(java.util.Optional.of(record2));
		
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete("book/books/2")
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk());
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
