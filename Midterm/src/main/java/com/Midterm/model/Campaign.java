package com.Midterm.model;

import java.util.Arrays;

public class Campaign {
    private Influencer influencer;
    private Post[] posts;

    public Campaign(Influencer influencer, Post[] posts) {
        this.influencer = influencer;
        this.posts = posts;
    }

    public Influencer getInfluencer() {
        return influencer;
    }

    public Post[] getPosts() {
        return posts;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "influencer=" + influencer +
                ", posts=" + Arrays.toString(posts) +
                '}';
    }
}