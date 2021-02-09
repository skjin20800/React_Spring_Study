package com.cos.book3.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cos.book3.domain.BookRepository;
import com.cos.book3.service.BookService;

/**
 *단위 테스트 ( Service와 관련된 애들만 메모리에 띄우면 됨.)
 *BoardRepository => 가짜 객체로 만들 수 있음.
 *
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	@InjectMocks // BookService객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받는다.
	private BookService bookService;
	
	@Mock
	private BookRepository bookRepository;
	
}
