package com.itheima.dao;

import com.itheima.pojo.Book;

import java.util.List;

/**
 * @author TanLin
 * @date 2019/11/21 16:54
 * @className com.itheima.dao
 * @projectName lucene
 */
public interface BookDao {
    List<Book> findAll();
}
