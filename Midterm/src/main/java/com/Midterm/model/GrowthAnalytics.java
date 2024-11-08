package com.Midterm.model;

public class GrowthAnalytics implements Analyticable, Comparable<GrowthAnalytics> {

    private int currentLikes;
    private int comments;
    private int currentFollowers;
    private int previousFollowers;

    public GrowthAnalytics(int currentLikes, int comments, int currentFollowers, int previousFollowers) {
        this.currentLikes = currentLikes;
        this.comments = comments;
        this.currentFollowers = currentFollowers;
        this.previousFollowers = previousFollowers;
    }

    @Override
    public void analyze() {
        // Implementation of analysis logic
        System.out.println("Performing growth analysis...");
    }

    @Override
    public double calculateEngagementGrowth(int currentLikes, int previousLikes) {
        if (previousLikes == 0) {
            return 100.0;
        }
        return ((double) (currentLikes - previousLikes) / previousLikes) * 100;
    }

    @Override
    public double analyzeFollowerGrowth(int currentFollowers, int previousFollowers) {
        if (previousFollowers == 0) {
            return 100.0;
        }
        return ((double) (currentFollowers - previousFollowers) / previousFollowers) * 100;
    }

    @Override
    public int compareTo(GrowthAnalytics other) {
        int thisEngagement = this.currentLikes + this.comments;
        int otherEngagement = other.currentLikes + other.comments;
        return Integer.compare(thisEngagement, otherEngagement);
    }

    public int getCurrentLikes() {
        return currentLikes;
    }

    public void setCurrentLikes(int currentLikes) {
        this.currentLikes = currentLikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCurrentFollowers() {
        return currentFollowers;
    }

    public void setCurrentFollowers(int currentFollowers) {
        this.currentFollowers = currentFollowers;
    }

    public int getPreviousFollowers() {
        return previousFollowers;
    }

    public void setPreviousFollowers(int previousFollowers) {
        this.previousFollowers = previousFollowers;
    }
}