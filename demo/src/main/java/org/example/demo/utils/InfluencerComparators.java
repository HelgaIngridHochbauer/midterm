package org.example.demo.utils;

import org.example.demo.model.Influencer;

import java.util.Comparator;

public class InfluencerComparators {
    public static final Comparator<Influencer> BY_NAME = Comparator.comparing(Influencer::getName);
    public static final Comparator<Influencer> BY_FOLLOWERS_ASCENDING = Comparator.comparingInt(Influencer::getFollowers);
    public static final Comparator<Influencer> BY_FOLLOWERS_DESCENDING = Comparator.comparingInt(Influencer::getFollowers).reversed();
}