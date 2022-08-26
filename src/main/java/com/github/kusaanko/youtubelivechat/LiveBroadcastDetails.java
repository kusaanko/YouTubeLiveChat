package com.github.kusaanko.youtubelivechat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveBroadcastDetails {
    @SerializedName("isLiveNow")
    @Expose
    public Boolean isLiveNow;
    @SerializedName("startTimestamp")
    @Expose
    public String startTimestamp;
    @SerializedName("endTimestamp")
    @Expose
    public String endTimestamp;

    public Boolean getLiveNow() {
        return isLiveNow;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    @Override
    public String toString() {
        return "LiveBroadcastDetails{" +
                "isLiveNow=" + isLiveNow +
                ", startTimestamp='" + startTimestamp + '\'' +
                ", endTimestamp='" + endTimestamp + '\'' +
                '}';
    }
}
