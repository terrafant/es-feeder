package com.uay.elasticsearch.clients.springdata;

import com.uay.elasticsearch.clients.PostClient;
import com.uay.elasticsearch.model.Post;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uay.elasticsearch.model.Post.BODY_FIELD;
import static com.uay.elasticsearch.model.Post.KEYWORDS_FIELD;

@Service
public class PostSpringDataClient implements PostClient {

    @Autowired
    private PostSpringDataRepository postSpringDataRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void save(List<Post> posts) {
        postSpringDataRepository.save(posts);
    }

    @Override
    public List<Post> searchWithInSituAnalyzer(String query) {
        return search(QueryBuilders.matchPhraseQuery(BODY_FIELD, query));
    }

    @Override
    public List<Post> fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(QueryBuilders.boolQuery()
                .must(QueryBuilders.fuzzyQuery(BODY_FIELD, query).fuzziness(Fuzziness.AUTO))
                .filter(QueryBuilders.termQuery(KEYWORDS_FIELD, keyword))
        );
    }

    private List<Post> search(QueryBuilder queryBuilder) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withHighlightFields(new HighlightBuilder.Field(BODY_FIELD))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Post.class);
    }

}
