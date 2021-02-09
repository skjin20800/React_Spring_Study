package com.cos.book3.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book3.domain.Book;
import com.cos.book3.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;



// 단위테스트

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

@Autowired
private MockMvc mockMvc; //주소호출을해서 테스트해주는 도구

@MockBean
private BookService bookService;

//BDDMockito 패턴
@Test 
public void save_테스트() throws Exception {
	// given(테스트를 하기 위한 준비)
	Book book = new Book(null,"스프링 따라하기", "코스");
	String content = new ObjectMapper().writeValueAsString(book);
	log.info(content);
	when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링 따라하기", "코스"));

	//when(테스트 실행) //ResultAction -> 응답을 받을수있음
	ResultActions resultAction = mockMvc.perform(post("/book") //get,put,post등
			.contentType(MediaType.APPLICATION_JSON_UTF8)//던지는타입,contentType("applicaton/json")
			.content(content)//실제던질데이터
			.accept(MediaType.APPLICATION_JSON_UTF8));//응답받을 데이터
	
	//사용법 mockMvc.perform(post,get,put("/url")
	//          .contentType)
	
	//then (검증)
	resultAction
	.andExpect(status().isCreated())//(status의 결과값을, isCreated로 기대한다)
	.andExpect(jsonPath("$.title").value("스프링 따라하기"))//jsonPath - json을 리턴한다.//
	.andDo(MockMvcResultHandlers.print()); //결과를 콘솔에 보여준다
}


 @Test
 public void finalAll_테스트() throws Exception {
	 //given
	 List<Book> books = new ArrayList<>();
	 books.add(new Book(1L,"스프링부트 따라하기", "코스"));
	 books.add(new Book(2L,"리엑트 따라하기","코스"));
	 when(bookService.모두가져오기()).thenReturn(books);
	 
	 //when
	 ResultActions resultAction = mockMvc.perform(get("/book")
			 .accept(MediaType.APPLICATION_JSON_UTF8));
	 
	 //then
	 resultAction
	 .andExpect(status().isOk())
	 .andExpect(jsonPath("$",Matchers.hasSize(2)))//결과값 2개를 기대
	 .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
	 .andDo(MockMvcResultHandlers.print());
			 
 }
 
 @Test
 public void findById_테스트() throws Exception{
	 //given	 
	 Long id = 1L;
	 when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기","쌀"));
	 
	 //when
	 ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
			 .accept(MediaType.APPLICATION_JSON_UTF8));
	 
	 //then
	 resultAction
	 .andExpect(status().isOk())
	 .andExpect(jsonPath("$.title").value("자바 공부하기"))
	 .andDo(MockMvcResultHandlers.print());
	 
 }
 
 @Test
 public void update_테스트() throws Exception{
	 //given
	 Long id = 1L;
	 Book book = new Book(null,"C++ 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);
		log.info(content);
		when(bookService.수정하기(id,book)).thenReturn(new Book(1L,"C++ 따라하기", "코스"));
	 
	 
	 //when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id) //get,put,post등
				.contentType(MediaType.APPLICATION_JSON_UTF8)//던지는타입,contentType("applicaton/json")
				.content(content)//실제던질데이터
				.accept(MediaType.APPLICATION_JSON_UTF8));//응답받을 데이터
		
	 
	 //then
	 resultAction
	 .andExpect(status().isOk())
	 .andExpect(jsonPath("$.title").value("C++ 따라하기"))
	 .andDo(MockMvcResultHandlers.print());
	 
 }
 
 @Test
 public void delete_테스트() throws Exception{
	 //given
	 Long id = 1L;
		when(bookService.삭제하기(id)).thenReturn("ok");
	 
	 //when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id) //get,put,post등
				.accept(MediaType.TEXT_PLAIN));//응답받을 데이터
		
	 //then
	 resultAction
	 .andExpect(status().isOk())
	 .andDo(MockMvcResultHandlers.print());
	 
	 //TEXT_PLAIN으로 응답시 아래와같이 사용
	 MvcResult requestResult = resultAction.andReturn();
	 String result = requestResult.getResponse().getContentAsString();
	 
	 assertEquals("ok",result);
 }
}
