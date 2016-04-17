package com.uay.elasticsearch.clients;

import static com.uay.elasticsearch.model.Blogpost.BODY_FIELD;
import static com.uay.elasticsearch.model.Blogpost.KEYWORDS_FIELD;

public class JsonQueryHolder {

    public static String constructQuerySearchRequest(String query) {
        return "{\n" +
                "    \"query\" : {\n" +
                "        \"match\": {\n" +
                "            \"" + BODY_FIELD +"\": {\n" +
                "                \"query\": \"" + query + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "     \"highlight\" : {\n" +
                "        \"fields\" : {\n" +
                "            \"" + BODY_FIELD + "\" : {}\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String constructSearchRequestWithInSituAnalyzer(String query) {
        return "{\n" +
                "    \"query\" : {\n" +
                "        \"match\": {\n" +
                "            \"" + BODY_FIELD +"\": {\n" +
                "                \"query\": \"" + query + "\",\n" +
                "                \"analyzer\": \"in_situ\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "     \"highlight\" : {\n" +
                "        \"fields\" : {\n" +
                "            \"" + BODY_FIELD + "\" : {}\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String constructFuzzySearchRequestWithKeywordFilter(String query, String keyword) {
        return "{\n" +
                "    \"query\" : {\n" +
                "        \"bool\" : {\n" +
                "            \"must\" : {\n" +
                "                \"match\": {\n" +
                "                    \"" + BODY_FIELD + "\": {\n" +
                "                        \"query\": \"" + query + "\",\n" +
                "                        \"fuzziness\": \"AUTO\"\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            \"filter\": {\n" +
                "                \"term\" : { \"" + KEYWORDS_FIELD + "\" : \"" + keyword + "\" }\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "     \"highlight\" : {\n" +
                "        \"fields\" : {\n" +
                "            \"" + BODY_FIELD + "\" : {}\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

}
