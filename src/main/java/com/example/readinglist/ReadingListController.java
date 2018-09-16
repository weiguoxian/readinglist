/**
 * 
 */
package com.example.readinglist;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author weiguoxian
 *
 */

@Controller
@RequestMapping("/readingList")
@ConfigurationProperties(prefix="amazon")
public class ReadingListController {
	
	static Logger logger = LoggerFactory.getLogger(ReadingListController.class.getName());
	
	private ReadingListRepository readingListRepository;
	private AmazonProperties amazonProperties;
	
	@Autowired
	public ReadingListController(ReadingListRepository readingListRepository,
			AmazonProperties amazonProperties) {
		
		this.readingListRepository = readingListRepository;
		this.amazonProperties = amazonProperties;
	}
	
	@RequestMapping(value="/{reader}", method=RequestMethod.GET)
	public String readersBooks(@PathVariable("reader") String reader, Model model) {
		
		List<Book> readingList = readingListRepository.findByReader(reader);
		if(readingList != null) {
			model.addAttribute("books", readingList);
			model.addAttribute("reader", reader);
			model.addAttribute("amazonID", amazonProperties.getAssociatedId());
		}
		logger.info("Get Reading List");
		return "readingList";
	}
	
	@RequestMapping(value="/{reader}", method=RequestMethod.POST)
	public String addToReadingList(@PathVariable("reader") String reader, Book book) {
		book.setReader(reader);
		readingListRepository.save(book);
		logger.info("Add book to Reading List");
		return "redirect:/readingList/{reader}";
	}
}
