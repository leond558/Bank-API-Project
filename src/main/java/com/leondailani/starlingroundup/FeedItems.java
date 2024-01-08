package com.leondailani.starlingroundup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedItems {
    private List<FeedItem> feedItems;

    // getters and setters
    public List<FeedItem> getFeedItems() {
        return feedItems;
    }

}
