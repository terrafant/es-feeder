package com.uay.elasticsearch.clients.springdata;

import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.BlogpostClient;
import com.uay.elasticsearch.model.Blogpost;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uay.elasticsearch.model.Blogpost.BODY_FIELD;
import static com.uay.elasticsearch.model.Blogpost.KEYWORDS_FIELD;

@Service(EsConstants.BLOGPOST_CLIENT)
@Profile("spring-data")
public class BlogpostSpringDataClient implements BlogpostClient {

    @Autowired
    private BlogpostSpringDataRepository postSpringDataRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
        postSpringDataRepository.save(blogposts);
    }

    private List<Blogpost> search(QueryBuilder queryBuilder) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withHighlightFields(new HighlightBuilder.Field(BODY_FIELD))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Blogpost.class);
    }

}
