package com.leondailani.starlingroundup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Represents the output of a transactions feed call to the Starling Bank API.
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
public class FeedItems {
    private List<FeedItem> feedItems;

    // getters and setters
    public List<FeedItem> getFeedItems() {
        return feedItems;
    }

}
