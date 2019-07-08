import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.packed.DirectReader;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class QueryIndex {
    @Test
    public void testSearch() throws IOException, ParseException {
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        //创建查询对象
        Query query = parser.parse("谷歌地图创始人拉斯");
        search(query);
    }

    //词条查询（精确查找）
    @Test
    public void testTermQuery() throws ParseException, IOException {
        //创建查询对象
        Query query = new TermQuery(new Term("title", "跳槽"));
        search(query);
    }

    //通配符查询
    // ? 匹配一个字符
    // * 匹配n个字符
    @Test
    public void testWildCardQuery() throws ParseException, IOException {
        //创建查询对象
        //Query query = new WildcardQuery(new Term("title", "?始"));
        Query query = new WildcardQuery(new Term("title", "创*"));
        search(query);
    }

    //模糊查询
    // 只能匹配少于两个字符错误的模糊查询
    @Test
    public void testFuzzyQuery() throws ParseException, IOException {
        //创建查询对象
        Query query = new FuzzyQuery(new Term("title", "fabcdebook"));
        search(query);
    }

    //数字范围才查询
    //  用于对非string类型的id进行精确的查询
    @Test
    public void testNumericRangeQuery() throws Exception{
        // 数值范围查询对象，参数：字段名称，最小值、最大值、是否包含最小值、是否包含最大值
        Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
        search(query);
    }

    //布尔查询
    /**
     *  布尔查询自身没有查询条件，可以把其他查询通过逻辑运算进行组合
     * 交集(and)：Occur.MUST + Occur.MUST
     * 并集(or)：Occur.SHOULD + Occur.SHOULD
     * 非(not)：Occur.MUST_NOT
     */
    @Test
    public void testBooleanQuery() throws Exception{
        //数字查询条件
        Query query1 = NumericRangeQuery.newLongRange("id", 1L, 3L, true, true);
        Query query2 = NumericRangeQuery.newLongRange("id", 2L, 4L, true, true);
        // 创建布尔查询的对象
        BooleanQuery query = new BooleanQuery();
        // 组合其它查询
        query.add(query1, BooleanClause.Occur.MUST_NOT);
        query.add(query2, BooleanClause.Occur.SHOULD);

        search(query);
    }


    private void search(Query query) throws IOException {
        //创建索引目录对象
        Directory directory = FSDirectory.open(new File("D:\\lucenetest"));
        //配置索引读取工具
        DirectoryReader reader = DirectoryReader.open(directory);
        //配置索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建查询解析器（默认要查询的字段名称，分词器）

        //搜索数据，两个参数：查询条件，对象要查询的最大结果条数
        //返回的结果是按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
        TopDocs topDocs = searcher.search(query, 10);
        //获取总条数
        System.out.println(topDocs.totalHits);
        //获取文档对象数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //获取文档编号
            int docId = scoreDoc.doc;
            //根据编号查找对应的文档
            Document doc = searcher.doc(docId);
            System.out.println("id:" + doc.get("id"));
            System.out.println("title:" + doc.get("title"));
        }
    }
}
