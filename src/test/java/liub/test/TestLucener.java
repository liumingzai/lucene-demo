package liub.test;

import com.liub.lucene.LuceneHelper;
import com.liub.lucene.LuceneIndexTypeEnum;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: sjx
 * Date: 13-6-1
 * Time: 下午12:54
 * To change this template use File | Settings | File Templates.
 */
public class TestLucener
{

    @Test
    public void testIndexDoc() {
        try {
            Document document1 = new Document();
            document1.add(new StringField("name", "张三", Field.Store.YES));
            document1.add(new IntField("sex", 0, Field.Store.YES));
            document1.add(new IntField("age", 22, Field.Store.YES));
            document1.add(new StringField("address", "上海", Field.Store.YES));
            document1.add(new StringField("email", "zhangsan@qq.com", Field.Store.YES));
            LuceneHelper.index(LuceneIndexTypeEnum.USER, document1);

            Document document2 = new Document();
            document2.add(new StringField("name", "李四", Field.Store.YES));
            document2.add(new IntField("sex", 1, Field.Store.YES));
            document2.add(new IntField("age", 35, Field.Store.YES));
            document2.add(new StringField("address", "北京", Field.Store.YES));
            document2.add(new StringField("email", "lisi@qq.com", Field.Store.YES));
            LuceneHelper.index(LuceneIndexTypeEnum.USER, document2);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testSearch() {
        try {
            IndexSearcher indexSearcher = LuceneHelper.getIndexSearcher( LuceneIndexTypeEnum.USER );
            TermQuery termQuery = new TermQuery(new Term("email", "zhangsan@qq.com"));
            TopDocs topDocs = indexSearcher.search(termQuery, 2);
            int count = topDocs.scoreDocs.length;
            for(int i = 0; i < count; i++){
                System.out.println("-----------" + i + "----------");
                Document document = indexSearcher.doc(topDocs.scoreDocs[i].doc);
                System.out.println(document.get("name"));
                System.out.println(document.get("sex"));
                System.out.println(document.get("age"));
                System.out.println(document.get("address"));
                System.out.println(document.get("email"));
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
