package com.uay.elasticsearch.clients.esnative;

import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.BlogpostClient;
import com.uay.elasticsearch.model.Blogpost;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.uay.elasticsearch.model.Blogpost.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service(EsConstants.BLOGPOST_CLIENT)
@Profile("native")
public class BlogpostEsNativeClient implements BlogpostClient {

    private static final Logger logger = LoggerFactory.getLogger(BlogpostEsNativeClient.class);

    @Autowired
    private Client client;

    @Override
    public List<Blogpost> searchQuery(String query) {
        return search(QueryBuilders.matchPhraseQuery(BODY_FIELD, query));
    }

    @Override
    public List<Blogpost> searchWithInSituAnalyzer(String query) {
        return search(QueryBuilders.matchPhraseQuery(BODY_FIELD, query).analyzer("in_situ"));
    }

    @Override
    public List<Blogpost> fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(QueryBuilders.boolQuery()
                .must(QueryBuilders.fuzzyQuery(BODY_FIELD, query).fuzziness(Fuzziness.AUTO))
                .filter(QueryBuilders.termQuery(KEYWORDS_FIELD, keyword))
        );
    }

    @Override
    public void save(List<Blogpost> blogposts) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        blogposts.stream()
                .forEach(post -> bulkRequest.add(constructIndexRequestBuilder(client, post)));

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        logger.info("Data was successfully imported");
        logger.debug("Bulk request took: " + bulkResponse.getTook());
        if (bulkResponse.hasFailures()) {
            logger.error("Failure in bulkResponse = " + bulkResponse);
        }
    }

    public boolean createIndexType(String index, String type) {
        return client.admin().indices()
                .prepareCreate(index)
                .setSettings(buildSettings()).addMapping(type, buildMappings(type))
                .execute().actionGet()
                .isAcknowledged();
    }

    private XContentBuilder buildSettings() {
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                        .startObject("analysis")
                            .startObject("filter")
                                .startObject("unique_stem")
                                    .field("type", "unique")
                                    .field("only_on_same_position", true)
                                .endObject()
                                .startObject("synonym")
                                    .field("type", "synonym")
                                    .field("format", "wordnet")
                                    .field("synonyms_path", "wn_s.pl")
                                .endObject()
                            .endObject()
                            .startObject("analyzer")
                                .startObject("in_situ")
                                    .field("tokenizer", "standard")
                                    .field("filter")
                                            .startArray()
                                                .value("lowercase")
                                                .value("keyword_repeat")
                                                .value("porter_stem")
                                                .value("synonym")
                                                .value("unique_stem")
                                            .endArray()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapping;
    }

    private XContentBuilder buildMappings(String type) {
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                        .startObject(type)
                            .startObject("properties")
                                .startObject("author")
                                    .field("type", "string")
                                .endObject()
                                .startObject("body")
                                    .field("type", "string")
                                .endObject()
                                .startObject("date")
                                    .field("type", "date")
                                    .field("format", "strict_date_optional_time||epoch_millis")
                                .endObject()
                                .startObject("keywords")
                                    .field("type", "string")
                                .endObject()
                                .startObject("title")
                                    .field("type", "string")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapping;
    }

    private List<Blogpost> search(QueryBuilder queryBuilder) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsConstants.INDEX)
                .setTypes(EsConstants.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addHighlightedField(BODY_FIELD);

        logger.debug("ElasticSearch Query using Java Client API:\n " + searchRequestBuilder.internalBuilder());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        return transformToPosts(searchResponse);
    }

    private List<Blogpost> transformToPosts(SearchResponse searchResponse) {
        List<Blogpost> blogposts = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            blogposts.add(new Blogpost(hit.getSource()));
        }
        return blogposts;
    }

    private IndexRequestBuilder constructIndexRequestBuilder(Client client, Blogpost blogpost) {
        try {
            return client
                    .prepareIndex(EsConstants.INDEX, EsConstants.TYPE)
                    .setSource(jsonBuilder()
                                    .startObject()
                                        .field(DATE_FIELD, blogpost.getDate())
                                        .field(AUTHOR_FIELD, blogpost.getAuthor())
                                        .field(BODY_FIELD, blogpost.getBody())
                                        .field(TITLE_FIELD, blogpost.getTitle())
                                        .field(KEYWORDS_FIELD, blogpost.getKeywords())
                                    .endObject()
                    );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
