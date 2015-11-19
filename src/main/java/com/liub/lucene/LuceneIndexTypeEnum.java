package com.liub.lucene;

/**
 * Created with IntelliJ IDEA.
 * User: sjx
 * Date: 13-6-1
 * Time: 上午11:01
 * To change this template use File | Settings | File Templates.
 */
public enum LuceneIndexTypeEnum {

    USER("user");

    private String indexTypeName;

    private LuceneIndexTypeEnum(String indexTypeName) {
        this.indexTypeName = indexTypeName;
    }

    public String getIndexTypeName() {
        return indexTypeName;
    }

    public void setIndexTypeName(String indexTypeName) {
        this.indexTypeName = indexTypeName;
    }

}
