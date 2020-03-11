package com.yyan.activiti;


import com.yyan.App;
import com.yyan.pojo.Book;
import com.yyan.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {App.class})
public class BookTest {


    @Autowired
    private BookService bookService;

    /**
     * 验证数据库
     */
    @Test
    public void testInsertBook() {

        Book book = new Book();
        book.setTitle("java");
        book.setPage(200);
        bookService.insertBook(book);
    }


}
