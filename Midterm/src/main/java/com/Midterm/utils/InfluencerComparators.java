package com.Midterm.utils;

import com.Midterm.model.Influencer;

import java.util.Comparator;

public class InfluencerComparators {
    public static final Comparator<Influencer> BY_NAME = Comparator.comparing(Influencer::getName);
    public static final Comparator<Influencer> BY_FOLLOWERS_ASCENDING = Comparator.comparingInt(Influencer::getFollowers);
    public static final Comparator<Influencer> BY_FOLLOWERS_DESCENDING = Comparator.comparingInt(Influencer::getFollowers).reversed();
}