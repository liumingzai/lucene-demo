package com.liub.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: sjx
 * Date: 13-6-1
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class Lucener {

    private static final String LUCENE_INDEX_DIR_BASE_PATH = "D://temp//lucene";
    private Version version = Version.LUCENE_43;
    private StandardAnalyzer standardAnalyzer = new StandardAnalyzer(version);

    private LuceneIndexTypeEnum luceneIndexTypeEnum;
    private Directory directory;
    private SearcherManager searcherManager;
    private TrackingIndexWriter trackingIndexWriter;
    private IndexWriter indexWriter;
    private SearcherFactory searcherFactory = new SearcherFactory();
    private NRTManagerReopenThread nrtManagerReopenThread;
    private NRTManager nrtManager;

    public Lucener( LuceneIndexTypeEnum luceneIndexTypeEnum) throws IOException {
        this.luceneIndexTypeEnum = luceneIndexTypeEnum;
        this.build();
    }

    private void build() throws IOException {
        buildDirectory();
        buildTrackingIndexWriter();
        buildNRTManagerReopenThread();
        buildSearcherManager();
    }

    private void buildDirectory() throws IOException {
        File indexDirPath = new File(this.LUCENE_INDEX_DIR_BASE_PATH + "//" + this.luceneIndexTypeEnum.getIndexTypeName());
        FileUtils.forceMkdir( indexDirPath );
        this.directory = FSDirectory.open(indexDirPath);
    }

    private LogMergePolicy createLogMergePolicy() {
        LogMergePolicy mergePolicy = new LogDocMergePolicy();
        //索引基本配置
        //设置segment添加文档(Document)时的合并频率
        //值较小,建立索引的速度就较慢
        //值较大,建立索引的速度就较快,>10适合批量建立索引
        mergePolicy.setMergeFactor(5);
        //设置segment最大合并文档(Document)数
        //值较小有利于追加索引的速度
        //值较大,适合批量建立索引和更快的搜索
        mergePolicy.setMaxMergeDocs(1000);
        //启用复合式索引文件格式,合并多个segment
        mergePolicy.setUseCompoundFile(true);
        return mergePolicy;
    }

    private void buildTrackingIndexWriter() throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(this.version, this.standardAnalyzer);
        indexWriterConfig.setMaxBufferedDocs(10000);
        indexWriterConfig.setMergePolicy(createLogMergePolicy());
        indexWriterConfig.setRAMBufferSizeMB(50);
        ///设置索引的打开模式 创建或者添加索引
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        //如果索引文件被锁，解锁索引文件
        if (IndexWriter.isLocked(this.directory)) {
            IndexWriter.unlock(this.directory);
        }
        //创建索引器
        IndexWriter indexWriter = new IndexWriter(this.directory, indexWriterConfig);
        this.trackingIndexWriter = new TrackingIndexWriter(indexWriter);
        this.indexWriter = indexWriter;
        //最开始创建索引时必须先提交，不然引起读取方法报错
        this.indexWriter.commit();
    }

    private void buildNRTManagerReopenThread() throws IOException {
        NRTManager nrtManager = new NRTManager(this.trackingIndexWriter, this.searcherFactory, true);
        //创建IndexWriter 写入监听线程 5.0为创建5个线程，执行频率为0.025秒
        NRTManagerReopenThread nrtManagerReopenThread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
        nrtManagerReopenThread.setName(this.luceneIndexTypeEnum.getIndexTypeName());
        nrtManagerReopenThread.setDaemon(true);
        nrtManagerReopenThread.start();
        this.nrtManager = nrtManager;
        this.nrtManagerReopenThread = nrtManagerReopenThread;
    }

    private void buildSearcherManager() throws IOException {
        this.searcherManager = new SearcherManager(this.directory, this.searcherFactory);
    }

    public Version getVersion() {
        return version;
    }

    public StandardAnalyzer getStandardAnalyzer() {
        return standardAnalyzer;
    }

    public LuceneIndexTypeEnum getLuceneIndexTypeEnum() {
        return luceneIndexTypeEnum;
    }

    public Directory getDirectory() {
        return directory;
    }

    public SearcherManager getSearcherManager() {
        return searcherManager;
    }

    public TrackingIndexWriter getTrackingIndexWriter() {
        return trackingIndexWriter;
    }

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public SearcherFactory getSearcherFactory() {
        return searcherFactory;
    }

    public NRTManagerReopenThread getNrtManagerReopenThread() {
        return nrtManagerReopenThread;
    }

    public NRTManager getNrtManager() {
        return nrtManager;
    }
}
