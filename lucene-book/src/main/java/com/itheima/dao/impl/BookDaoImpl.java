package com.itheima.dao.impl;

import com.itheima.dao.BookDao;
import com.itheima.pojo.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TanLin
 * @date 2019/11/21 16:55
 * @className com.itheima.dao.impl
 * @projectName lucene
 */
public class BookDaoImpl implements BookDao {
    @Override
    public List<Book> findAll() {
        // 数据库链接
        Connection connection = null;

        // 预编译statement
        PreparedStatement preparedStatement = null;

        // 结果集
        ResultSet resultSet = null;

        // 图书列表
        List<Book> list = new ArrayList<Book>();

        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene", "root", "root");

            // SQL语句
            String sql = "SELECT * FROM book";
            // 创建preparedStatement
            preparedStatement = connection.prepareStatement(sql);

            // 获取结果集
            resultSet = preparedStatement.executeQuery();

            // 结果集解析
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setPrice(resultSet.getFloat("price"));
                book.setPic(resultSet.getString("pic"));
                book.setDescription(resultSet.getString("description"));
                list.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement!=null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
