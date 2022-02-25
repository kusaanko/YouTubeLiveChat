package com.github.kusaanko.youtubelivechat;

import java.util.List;

public class Emoji {
    protected String emojiId;
    protected List<String> shortcuts;
    protected List<String> searchTerms;
    protected String iconURL;
    protected boolean isCustomEmoji;

    public String getEmojiId() {
        return emojiId;
    }

    public List<String> getShortcuts() {
        return shortcuts;
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    public String getIconURL() {
        return iconURL;
    }

    public boolean isCustomEmoji() {
        return isCustomEmoji;
    }

    @Override
    public String toString() {
        return "Emoji{" +
                "emojiId='" + emojiId + '\'' +
                ", shortcuts=" + shortcuts +
                ", searchTerms=" + searchTerms +
                ", iconURL='" + iconURL + '\'' +
                ", isCustomEmoji=" + isCustomEmoji +
                '}';
    }
}
