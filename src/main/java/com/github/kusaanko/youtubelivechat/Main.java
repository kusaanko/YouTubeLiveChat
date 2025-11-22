package com.github.kusaanko.youtubelivechat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Please provide a video ID");
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        YouTubeLiveChat chat = new YouTubeLiveChat(args[0], true, IdType.VIDEO);

        int liveStatusCheckCycle = 0;
        while (true) {
            chat.update();
            for (ChatItem item : chat.getChatItems()) {
                System.out.println(format.format(new Date(item.getTimestamp() / 1000)) + " " + item.getType() + "["
                        + item.getAuthorName() + "]" + item.getAuthorType() + " " + item.getMessage());
            }
            liveStatusCheckCycle++;
            if (liveStatusCheckCycle >= 10) {
                // Calling this method frequently, cpu usage and network usage become higher
                // because this method requests a http request.
                if (!chat.getBroadcastInfo().isLiveNow) {
                    break;
                }
                liveStatusCheckCycle = 0;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
