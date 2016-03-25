package com.uay.elasticsearch;

public class EsConstants {

    public static final String CLUSTER_NAME = "elasticsearch";
    public static final String HOST_NAME = "localhost";
    public static final int NATIVE_PORT = 9300;
    public static final int REST_PORT = 9200;
    public static final String INDEX = "index_name";
    public static final String TYPE = "type_name";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String INDEX_TYPE_URL = "http://" + EsConstants.HOST_NAME + ":" + EsConstants.REST_PORT + "/" +
            EsConstants.INDEX + "/" + EsConstants.TYPE;
}

