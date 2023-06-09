package com.example.Junitdemo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {
	
	
	@Autowired
	BookRepository bookRepository;
	
	@GetMapping("/books")
	public List<Book> getAllBookRecords(){
		return bookRepository.findAll();
	}
	
	@GetMapping("/books/{bookId}")
	public Book getBookById(@PathVariable(value ="bookId") Long bookId) {
		
		return bookRepository.findById(bookId).get();
	} 
	
	@PostMapping("/books")
	public Book createBookRecord(@RequestBody Book bookRecord) {
		
		return bookRepository.save(bookRecord);	
	}
	
	@PutMapping("/books")
	public Book updateBookRecord(@RequestBody Book bookRecord) throws NotFoundException{
		
		if(bookRecord == null ) {
			throw new NotFoundException();
			}
		
		Optional<Book> optionalBook = bookRepository.findById((Long) bookRecord.getBookId());
		if(!optionalBook.isPresent()) {
			throw  new NotFoundException();
		}
		
		Book existingBookRecord = optionalBook.get();
		existingBookRecord.setName(bookRecord.getName());
		existingBookRecord.setSummary(bookRecord.getSummary());
		existingBookRecord.setRating(bookRecord.getRating());
		
		
		return bookRepository.save(existingBookRecord);
		
	}
	
	
	@DeleteMapping("/books/{bookId}")
	public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException{
		
		if(!bookRepository.findById(bookId).isPresent()) {
			throw new NotFoundException();
		}
		
		bookRepository.deleteById(bookId);
			
	}
	
	
	
	
	
	
	
	
	
	
	

}
