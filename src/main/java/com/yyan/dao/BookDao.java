package com.yyan.dao;

import com.yyan.pojo.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookDao {
    void insertBook(Book book); // 添加用户

}
