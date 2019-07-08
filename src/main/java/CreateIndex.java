import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CreateIndex {
    @Test
    public void testCreate() throws IOException {
        //创建文档对象
        Document document = new Document();
        //添加字段信息（字段名称、字段值、是否存储）
        //Store.YES代表存储到文档列表。Store.NO代表不存储
        //StringField会创建索引，但不会被分词
        document.add(new StringField("id", "1", Field.Store.YES));
        //TextField创建索引又被分词
        //document.add(new TextField("title", "谷歌地图之父跳槽facebook", Field.Store.YES));
        document.add(new TextField("title", "传智播客碉堡了", Field.Store.YES));

        //StoredField既不创建索引，也不会被分词
        document.add(new StoredField("url", "http://www.jd.com/431"));

        //创建目录类，指定索引在硬盘中的位置
        File file = new File("D:\\lucenetest");
        Directory directory = FSDirectory.open(file);
        //创建分词器对象
        //StandardAnalyzer analyzer = new StandardAnalyzer();
        //创建Ik分词器对象
        IKAnalyzer analyzer = new IKAnalyzer();
        //索引写出工具的配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        /**
         * 配置写入模式
         *      CREATE：每次写入都覆盖以前的数据
         *      APPEND：不覆盖数据，而是使用以前的索引数据后追加
         *      CREATE_OR_APPEND：如果不存在则创建新的，如果存在则追加数据
         */
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //创建索引的写出工具类。参数，索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //把文档交给indexWriter管理
        indexWriter.addDocument(document);
        //提交
        indexWriter.commit();
        //关闭
        indexWriter.close();

    }

    // 批量创建索引
    @Test
    public void testCreate2() throws Exception{
        // 创建文档的集合
        Collection<Document> docs = new ArrayList<>();
        // 创建文档对象
        Document document1 = new Document();
        document1.add(new LongField("id", 1, Store.YES));
        document1.add(new TextField("title", "谷歌地图之父跳槽facebook", Store.YES));
        docs.add(document1);
        // 创建文档对象
        Document document2 = new Document();
        document2.add(new LongField("id", 2, Store.YES));
        document2.add(new TextField("title", "谷歌地图之父加盟FaceBook", Store.YES));
        docs.add(document2);
        // 创建文档对象
        Document document3 = new Document();
        document3.add(new LongField("id", 3, Store.YES));
        document3.add(new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook", Store.YES));
        docs.add(document3);
        // 创建文档对象
        Document document4 = new Document();
        document4.add(new LongField("id", 4, Store.YES));
        document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Store.YES));
        docs.add(document4);
        // 创建文档对象
        Document document5 = new Document();
        document5.add(new LongField("id", 5, Store.YES));
        document5.add(new TextField("title", "谷歌地图之父拉斯加盟社交网站Facebook", Store.YES));
        docs.add(document5);

        write(docs);
    }

    private void write(Collection<Document> docs) throws IOException {
        //创建目录类，指定索引在硬盘中的位置
        File file = new File("D:\\lucenetest");
        Directory directory = FSDirectory.open(file);
        //创建分词器对象
        //StandardAnalyzer analyzer = new StandardAnalyzer();
        //创建Ik分词器对象
        IKAnalyzer analyzer = new IKAnalyzer();
        //索引写出工具的配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        /**
         * 配置写入模式
         *      CREATE：每次写入都覆盖以前的数据
         *      APPEND：不覆盖数据，而是使用以前的索引数据后追加
         *      CREATE_OR_APPEND：如果不存在则创建新的，如果存在则追加数据
         */
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //创建索引的写出工具类。参数，索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //把文档交给indexWriter管理
        indexWriter.addDocuments(docs);
        //提交
        indexWriter.commit();
        //关闭
        indexWriter.close();
    }
}
