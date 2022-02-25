package com.github.kusaanko.youtubelivechat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatItem {
    protected ChatItemType type;
    protected String authorName;
    protected String authorChannelID;
    protected String message;
    protected List<Object> messageExtended;
    protected String authorIconURL;
    protected String id;
    protected long timestamp;
    protected List<AuthorType> authorType;
    protected String memberBadgeIconURL;
    //For paid message
    protected int bodyBackgroundColor;
    protected int bodyTextColor;
    protected int headerBackgroundColor;
    protected int headerTextColor;
    protected int authorNameTextColor;
    protected String purchaseAmount;
    //For paid sticker
    protected String stickerIconURL;
    protected int backgroundColor;
    //For ticker paid message
    protected int endBackgroundColor;
    protected int durationSec;
    protected int fullDurationSec;
    //If moderator enabled
    protected String contextMenuParams;
    protected String pinToTopParams;
    protected String chatDeleteParams; // can be executed by author too
    protected String timeBanParams;
    protected String userBanParams;
    protected String userUnbanParams;
    //Connected chat
    protected YouTubeLiveChat liveChat;

    /**
     * @deprecated {@link #ChatItem(YouTubeLiveChat liveChat)}
     */
    @Deprecated
    protected ChatItem() {
        this(null);
    }

    protected ChatItem(YouTubeLiveChat liveChat) {
        this.authorType = new ArrayList<>();
        this.authorType.add(AuthorType.NORMAL);
        this.type = ChatItemType.MESSAGE;
        this.liveChat = liveChat;
    }

    /**
     * Get type of this item.
     *
     * @return Type of this item
     */
    public ChatItemType getType() {
        return this.type;
    }

    /**
     * Get author name.
     *
     * @return Author name
     */
    public String getAuthorName() {
        return this.authorName;
    }

    /**
     * Get author's channel id.
     *
     * @return Author's channel id
     */
    public String getAuthorChannelID() {
        return this.authorChannelID;
    }

    /**
     * Get message in String
     *
     * @return Message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Get list of extended messages.
     * This list contains Text or Emoji.
     *
     * @return List of extended messages
     */
    public List<Object> getMessageExtended() {
        return this.messageExtended;
    }

    /**
     * Get author's icon url.
     *
     * @return Author's icon url
     */
    public String getAuthorIconURL() {
        return this.authorIconURL;
    }

    /**
     * Get id.
     *
     * @return Id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get timestamp of this item.
     *
     * @return Timestamp in UNIX time
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * Get author types in List.
     *
     * @return List of AuthorType
     */
    public List<AuthorType> getAuthorType() {
        return this.authorType;
    }

    /**
     * Is this message's author verified?
     *
     * @return If this message's author is verified, returns true.
     */
    public boolean isAuthorVerified() {
        return this.authorType.contains(AuthorType.VERIFIED);
    }

    /**
     * Is this message's author owner?
     *
     * @return If this message's author is owner, returns true.
     */
    public boolean isAuthorOwner() {
        return this.authorType.contains(AuthorType.OWNER);
    }

    /**
     * Is this message's author moderator?
     *
     * @return If this message's author is moderator, returns true.
     */
    public boolean isAuthorModerator() {
        return this.authorType.contains(AuthorType.MODERATOR);
    }

    /**
     * Is this message's author member?
     *
     * @return If this message's author is member, returns true.
     */
    public boolean isAuthorMember() {
        return this.authorType.contains(AuthorType.MEMBER);
    }

    /**
     * Get member badge icon url.
     * You can use if isAuthorMember() == true
     *
     * @return Member badge icon url
     */
    public String getMemberBadgeIconURL() {
        return this.memberBadgeIconURL;
    }

    /**
     * Get background color of body in int.
     *
     * @return Color in int
     */
    public int getBodyBackgroundColor() {
        return this.bodyBackgroundColor;
    }

    /**
     * Get text color of background in int.
     *
     * @return Color in int
     */
    public int getBodyTextColor() {
        return this.bodyTextColor;
    }

    /**
     * Get header background color in int.
     *
     * @return Color in int
     */
    public int getHeaderBackgroundColor() {
        return this.headerBackgroundColor;
    }

    /**
     * Get header color in int.
     *
     * @return Color in int
     */
    public int getHeaderTextColor() {
        return this.headerTextColor;
    }

    /**
     * Get purchase amount of this paid message
     *
     * @return Amount of money(example ￥100)
     */
    public String getPurchaseAmount() {
        return this.purchaseAmount;
    }

    /**
     * Get text color of drawing author name in int.
     *
     * @return Color in int
     */
    public int getAuthorNameTextColor() {
        return this.authorNameTextColor;
    }

    public int getEndBackgroundColor() {
        return this.endBackgroundColor;
    }

    /**
     * Get elapsed time from starting viewing this paid message in seconds.
     * You can use if getType() == PAID_MESSAGE
     *
     * @return Elapsed time from starting viewing this paid message in seconds
     */
    public int getDurationSec() {
        return this.durationSec;
    }

    /**
     * Get full duration of paid message viewing in seconds.
     * You can use if getType() == PAID_MESSAGE
     *
     * @return Full duration of paid message viewing in seconds
     */
    public int getFullDurationSec() {
        return this.fullDurationSec;
    }

    /**
     * Get sticker icon url.
     * You can use if getType() == PAID_STICKER
     *
     * @return Sticker icon url
     */
    public String getStickerIconURL() {
        return stickerIconURL;
    }

    /**
     * Get background color in int.
     * You can use if getType() == PAID_STICKER
     *
     * @return Background color in int
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "type=" + type +
                ", authorName='" + authorName + '\'' +
                ", authorChannelID='" + authorChannelID + '\'' +
                ", message='" + message + '\'' +
                ", messageExtended=" + messageExtended +
                ", iconURL='" + authorIconURL + '\'' +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", authorType=" + authorType +
                ", memberBadgeIconURL='" + memberBadgeIconURL + '\'' +
                ", bodyBackgroundColor=" + bodyBackgroundColor +
                ", bodyTextColor=" + bodyTextColor +
                ", headerBackgroundColor=" + headerBackgroundColor +
                ", headerTextColor=" + headerTextColor +
                ", authorNameTextColor=" + authorNameTextColor +
                ", purchaseAmount='" + purchaseAmount + '\'' +
                ", stickerIconURL='" + stickerIconURL + '\'' +
                ", backgroundColor=" + backgroundColor +
                ", endBackgroundColor=" + endBackgroundColor +
                ", durationSec=" + durationSec +
                ", fullDurationSec=" + fullDurationSec +
                '}';
    }

    /**
     * Delete this chat.
     * You need to set user data using setUserData() before calling this method.
     * User must be either author of chat, moderator or owner.
     *
     * @throws IOException           Http request error
     * @throws IllegalStateException The IDs are not set or permission denied error
     */
    public void delete() throws IOException {
        liveChat.deleteMessage(this);
    }

    /**
     * Ban chat author for 300 seconds (+ delete chat).
     * You need to set user data using setUserData() before calling this method.
     * User must be either moderator or owner.
     *
     * @throws IOException           Http request error
     * @throws IllegalStateException The IDs are not set or permission denied error
     */
    public void timeoutAuthor() throws IOException {
        liveChat.banAuthorTemporarily(this);
    }

    /**
     * Ban chat author permanently from the channel (+ delete chat).
     * You need to set user data using setUserData() before calling this method.
     * User must be either moderator or owner.
     * <br>
     * <b>**Use with cautions!!**</b> It is recommended to store these banned ChatItem so you can unban later.
     *
     * @throws IOException           Http request error
     * @throws IllegalStateException The IDs are not set or permission denied error
     */
    public void banAuthor() throws IOException {
        liveChat.banUserPermanently(this);
    }

    /**
     * Unban chat author who was permanently banned from the channel (deleted chat won't be recovered).
     * You need to set user data using setUserData() before calling this method.
     * User must be either moderator or owner.
     *
     * @throws IOException           Http request error
     * @throws IllegalStateException The IDs are not set or permission denied error
     */
    public void unbanAuthor() throws IOException {
        liveChat.unbanUser(this);
    }


    /**
     * Pin this chat as banner.
     * You need to set user data using setUserData() before calling this method.
     * User must be either moderator or owner.
     *
     * @throws IOException           Http request error
     * @throws IllegalStateException The IDs are not set or permission denied error
     */
    public void pinAsBanner() throws IOException {
        liveChat.pinMessage(this);
    }
}
