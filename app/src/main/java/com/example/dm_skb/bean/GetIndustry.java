package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/10/8.
 */
import java.util.List;

public class GetIndustry {
    private List<Industry> industry;

    public List<Channel> getChannel() {
        return channel;
    }

    public void setChannel(List<Channel> channel) {
        this.channel = channel;
    }

    public List<Industry> getIndustry() {
        return industry;
    }

    public void setIndustry(List<Industry> industry) {
        this.industry = industry;
    }

    private List<Channel> channel;


}
