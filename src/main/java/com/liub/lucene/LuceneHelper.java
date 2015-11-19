package com.liub.lucene;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sjx
 * Date: 13-6-1
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class LuceneHelper {

    private static final Log log = LogFactory.getLog( LuceneHelper.class );
    private static final Map<LuceneIndexTypeEnum, Lucener> type2Lucener = new HashMap<LuceneIndexTypeEnum, Lucener>();

    static  {
        for ( LuceneIndexTypeEnum luceneIndexTypeEnum : LuceneIndexTypeEnum.values()) {
            try {
                type2Lucener.put(luceneIndexTypeEnum, new Lucener(luceneIndexTypeEnum));
            } catch (IOException e) {
                log.error("LuceneHelper init error, indexTypeName:" + luceneIndexTypeEnum.getIndexTypeName(), e);
            }
        }
        System.out.println("LuceneHelper type2Lucener init finish.");
    }

    private LuceneHelper() {
    }

    public static void index( LuceneIndexTypeEnum luceneIndexTypeEnum, Document document) throws IOException {
        Lucener lucener = type2Lucener.get(luceneIndexTypeEnum);
        lucener.getTrackingIndexWriter().addDocument(document);
        lucener.getIndexWriter().commit();
        refresh(lucener);
    }

    public static IndexSearcher getIndexSearcher( LuceneIndexTypeEnum luceneIndexTypeEnum) throws IOException {
        Lucener lucener = type2Lucener.get(luceneIndexTypeEnum);
        return lucener.getSearcherManager().acquire();
    }

    private static void refresh( Lucener lucener) throws IOException {
        lucener.getSearcherManager().maybeRefresh();
        lucener.getNrtManager().maybeRefresh();
    }

}
