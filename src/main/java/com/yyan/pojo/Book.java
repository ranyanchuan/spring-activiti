package com.yyan.pojo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class Book {
    private Integer id;

    @Length(min = 2, max = 10, message = "书名不不能小于2位或者大于10位") // 验证字符串的大小
    @NotEmpty(message = "书名不能为空") // 非空校验 不去掉首尾空格
    private String title;

    @Min(value = 1, message = "页码不能小于1") // 校验数字不能小于
    @Max(value = 100, message = "页码不能大于500") // 校验数字不能大于
    private Integer page;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", page=" + page +
                '}';
    }

    public Book(Integer id, @Length(min = 2, max = 10, message = "书名不不能小于2位或者大于10位") @NotEmpty(message = "书名不能为空") String title, @Min(value = 1, message = "页码不能小于1") @Max(value = 100, message = "页码不能大于500") Integer page) {
        this.id = id;
        this.title = title;
        this.page = page;
    }

    public Book() {
    }
}
