package com.itheima;

import com.itheima.dao.BookDao;
import com.itheima.dao.impl.BookDaoImpl;
import com.itheima.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TanLin
 * @date 2019/11/21 16:59
 * @className com.itheima
 * @projectName lucene
 */

public class BookIndex {
    /**
     * 收集数据、创建文档，分析文档，创建索引的流程
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        BookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.findAll();
        List<Document> list = new ArrayList<>();
        for (Book book : bookList) {
            Document document = new Document();
            Field id = new TextField("id",book.getId().toString(), Field.Store.YES);
            TextField name = new TextField("name", book.getName(), Field.Store.YES);
            TextField price = new TextField("price", book.getPrice().toString(), Field.Store.YES);
            TextField pic = new TextField("pic", book.getPic(), Field.Store.YES);
            document.add(id);
            document.add(name);
            document.add(price);
            document.add(pic);
            list.add(document);
        }
//        磁盘得存储位置
        FSDirectory directory = FSDirectory.open(new File("D:\\java-2019-06\\aaa\\text"));
//        分词器
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
//       创建一个indexWriterConfig对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
//        将上面的Document集合遍历写入到硬盘
        for (Document indexableFields : list) {
            writer.addDocument(indexableFields);
        }
        writer.commit();
        writer.close();
    }

    /**
     * 搜索数据
     */
    @Test
    public void doIndexSearch() throws IOException {
//        指定搜索得磁盘位置
        FSDirectory directory = FSDirectory.open(new File("D:\\java-2019-06\\aaa\\text"));
//        创建indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
//        创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//        搜索得域和关键字
        TermQuery termQuery = new TermQuery(new Term("name", "java"));
//        执行
        TopDocs search = indexSearcher.search(termQuery, 100);
//        满足条件得总数
        int totalHits = search.totalHits;
        System.out.println(totalHits);
        ScoreDoc[] scoreDocs = search.scoreDocs;
//       查询的内容
        for (ScoreDoc scoreDoc : scoreDocs) {
//            文档得ID
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            System.out.println(document.get("name"));
            System.out.println(document.get("id"));
            System.out.println(document.get("price"));
            System.out.println(document.get("pic"));
        }
        indexReader.close();
    }


    /**
     * 更新
     */
    @Test
    public void updetele() throws IOException {
//        指定文档位置
        FSDirectory directory = FSDirectory.open(new File("D:\\java-2019-06\\aaa\\text"));
//        分析器
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
//        分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47,analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        Document document = new Document();
        document.add(new StringField("id","1", Field.Store.YES));
        document.add(new TextField("name","haha", Field.Store.YES));
        document.add(new LongField("price",00, Field.Store.YES));
        document.add(new StoredField("pic","dddd"));
        document.add(new TextField("description","aaaaaaaaaaaa", Field.Store.YES));

        indexWriter.updateDocument(new Term("name","spring"),document);
        indexWriter.close();

    }

    /**
     * 删除
     * @throws IOException
     */
    @Test
    public void delete() throws IOException {
        FSDirectory directory = FSDirectory.open(new File("D:\\java-2019-06\\aaa\\text"));
         Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
         IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47,analyzer);
         IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
         indexWriter.deleteDocuments(new Term("name","spring"));
         indexWriter.close();

    }

}
