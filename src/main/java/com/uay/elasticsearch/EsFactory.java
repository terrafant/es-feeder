package com.uay.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class EsFactory {

    private static final Logger logger = LoggerFactory.getLogger(EsFactory.class);

    private static final int DEFAULT_RETURN_SIZE = 10;

    private Client client;

    @PostConstruct
    public void setup() {
        client = createClient();
    }

    @PreDestroy
    public void cleanUp() {
        client.close();
    }

    public Client getClient() {
        if (client == null) {
            setup();
        }
        return client;
    }

    public SearchRequestBuilder createSearchRequestBuilder(String index, String type) {
        return getClient().prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setSize(DEFAULT_RETURN_SIZE);
    }

    private Client createClient() {
        Settings settings = Settings.settingsBuilder().put("cluster.name", EsConstants.CLUSTER_NAME).build();
        Client client =  null;
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_NAME), EsConstants.NATIVE_PORT)
                    );
        } catch (UnknownHostException e) {
            logger.error("Cannot create Elasticsearch client", e);
        }
        return client;
    }
}
