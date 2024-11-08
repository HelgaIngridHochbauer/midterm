package com.Midterm.model;

public interface Analyticable {
    void analyze();

    // Declare these methods as public
    public double calculateEngagementGrowth(int currentLikes, int previousLikes);
    public double analyzeFollowerGrowth(int currentFollowers, int previousFollowers);
}