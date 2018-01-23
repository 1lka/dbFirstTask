package com.dbbest.kirilenko.chainParser;

import com.dbbest.kirilenko.Tree.Node;

import java.util.regex.Pattern;

public class SearchParser extends AbstractChainParser {

    private static final String DEEP_SEARCH_PATTERN = "-ds|-d|-dsearch";
    private static final String WIDE_SEARCH_PATTERN = "-ws|-w|-wsearch";
    private static final String SEARCH_PATTERN = "-ws|-w|-wsearch|-ds|-d|-dsearch";


    @Override
    public void doWork(String[] args, Node node) {
        String searchType = null;
        String v1 = null;
        String v2 = null;
        for (int i = 0; i < args.length - 1; i++) {
            if (Pattern.matches(SEARCH_PATTERN, args[i])) {
                searchType = args[i];
                v1 = args[i + 1];
                if (args.length > i + 2) {
                    v2 = args[i + 2];
                }
            }
        }
        if (searchType == null) {
            if (nextUnit != null) {
                nextUnit.doWork(args, node);
            } else return;
        }
        Node wantedNode;
        if (Pattern.matches(DEEP_SEARCH_PATTERN, searchType)) {
            if (v2 == null) {
                wantedNode = node.deepSearch(v1);
            } else wantedNode = node.deepSearch(v1, v2);
        } else {
            if (v2 == null) {
                wantedNode = node.wideSearch(v1);
            } else wantedNode = node.wideSearch(v1, v2);
        }
        System.out.println(wantedNode);

        if (nextUnit != null) {
            nextUnit.doWork(args, node);
        }

    }


}
