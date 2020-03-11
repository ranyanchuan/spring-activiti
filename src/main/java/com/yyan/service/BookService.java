package com.yyan.service;

import com.yyan.dao.BookDao;
import com.yyan.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 添加事务
public class BookService {

    @Autowired
    private BookDao bookDao;

    /**
     * 添加 book
     *
     * @param book
     */
    public void insertBook(Book book) {
        bookDao.insertBook(book);
    }

}
