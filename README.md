# YouTubeLiveChat
 Java API that fetches YouTube live chat.

 # Requires
 - Java 8 or higher

 # Features
 - Fetch live chat without YouTube API
 - Fetch replay of live chat without YouTube API
 - Supports message, Super Chat, Super Stickers and member

# How to use

```Java
SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
YouTubeLiveChat chat = new YouTubeLiveChat("Aw5b1sa0w", true);
while (true) {
    chat.update();
    for (ChatItem item : chat.getChatItems()) {
        System.out.println(format.format(new Date(item.getTimestamp() / 1000)) + " " + item.getType() + "[" + item.getAuthorName() + "]" + item.getAuthorType() + " " + item.getMessage());
    }
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```
If this video is replay, you can get a lot of messages every time ```chat.update()```.  
Don't worry! There will be no duplicate messages.

## Get video id from URL
```Java
String videoId = YouTubeLiveChat.getVideoIdFromURL("https://www.youtube.com/watch?v=Aw5b1sa0w");
```

This method supports these links:
- www.youtube.com/watch?v=[VideoId]
- youtube.com/watch?v=[VideoId]
- www.youtube.com/watch?v=[VideoId]
- youtube.com/watch?v=[VideoId]
- youtu.be/[VideoId]
- www.youtube.com/embed/[VideoId]
- youtube.com/embed/[VideoId]

And these can include other parameters. For example
```
https://www.youtube.com/watch?v=[VideoId]&t=5s
```

## Set locale
```Java
chat.setLocale(Locale.JAPAN);
```

This affects some messages from YouTube. Exceptions are not localized.

## Get deleting item events
```Java
for (ChatItemDelete delete : chat.getChatItemDeletes()) {
    System.out.println(delete.getMessage() + " TargetId:" + delete.getTargetId());
}
```
If you needed to apply deleting messages from user, you would use this.  
To use this method, you must cache past items id.
```Java
ArrayList<String> cacheId = new ArrayList<>();
for (ChatItem item : chat.getChatItems()) {
    cacheId.add(item.getId());
}
for (ChatItemDelete delete : chat.getChatItemDeletes()) {
    if(cacheId.contains(delete.getTargetId())) {
        cacheId.remove(delete.getTargetId());
        //Delete message
    }
}
```

## Get video id
```Java
String videoId = chat.getVideoId();
```

## Get channel id
```Java
String channelId = chat.getChannelId();
//You can access this page
//"https://www.youtube.com/channel/" + channelId
```

## Check if this is replay
```Java
boolean isReplay = chat.isReplay();
```

## Skip in replay
```Java
int skipSec = 60;
//skip time in milli seconds
//This only works in replay
chat.update(skipSec * 1000);
```
To skip backward, please remake YouTubeLiveChat. Sorry, this API can not skip backward.


## ChatItem type
All types include these values:
- author name
- author channel id
- author icon url
- author type
- member badge icon url
- timestamp
- id

You can get a type of ChatItem using ```ChatItem#getType()```. This method returns ChatItemType.
- MESSAGE

This is normal message. It includes message and message extended.

- PAID_MESSAGE

This is called "Super Chat". It includes message, message extended, purchase amount, and a lot of colors.  
Purchase amount includes currency symbols.For example, "￥100" and "$100".

- PAID_STICKERS

This is called "Super Stickers". It includes sticker icon url and purchase amount.  
Purchase amount includes currency symbols.For example, "￥100" and "$100".

- TICKER_PAID_MESSAGE

This is what you can see at the top of the chat. It includes message, message extended, purchase amount, a lot of colors, duration and full duration. You can get items of this type using ```YouTubeLiveChat#getChatTickerPaidMessages()```.

- NEW_MEMBER_MESSAGE

This is used when new member is registered. It includes message and message extended.

## Author type
This shows what kind of certification the author has. For example, VERIFIED and OWNER.
You can get types of author using ```ChatItem#getAuthorType()```. This method returns AuthorType.  
Or you can use ```ChatItem#isAuthorVerified()```, ```ChatItem#isAuthorOwner()```, ```ChatItem#isAuthorModerator()``` and ```ChatItem#isAuthorMember()``` instead of ```ChatItem#getAuthorType()```.
For example of ChatItem#getAuthorType():
```Java
if (chatItem.getAuthorType().contains(AuthorType.OWNER)) {
    //If the author is owner
}
```

- VERIFIED

This indicates the author is verified.

- OWNER

This indicates the author is owner.

- MODERATOR

This indicates the author is moderator.

- MEMBER

This indicates the author is member. If the author is member, you can get member badge icon url using ```ChatItem#getMemberBadgeIconURL()```.

## Get emojis
Messages may include emojis. But you can't get emojis using ```ChatItem#getMessage()```. Normal message includes shortcuts instead of emoji. For example, YouTube icon is ":yt:".  
How to get emojis:
```Java
String message = "";
for (Object obj ; item.getMessageExtended()) {
    if (obj instanceof Text) {
        Text text = (Text) obj;
        message += text.getText();
    }
    if (obj instanceof Emoji) {
        Emoji emoji = (Emoji) obj;
        message += emoji.getShortcuts().get(0);
        //You can write program here
    }
}
```
Emoji can include more than one shortcut.  
To get emojis icon url, use ```Emoji#getIconURL()```. Download and use it!

# Desclaimer
I couldn't enough test some features (Paid Stickers, Ticker Paid Message and Member) because of lack of sample data. So there might some bugs. If you found issues, please notice me in GitHub issues!
