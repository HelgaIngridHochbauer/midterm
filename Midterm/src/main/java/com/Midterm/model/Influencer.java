package com.Midterm.model;

import java.io.Serializable;

public class Influencer implements Comparable<Influencer>, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String platform;
    private int followers;

    // Default constructor
    public Influencer() {
    }

    // Parameterized constructor
    public Influencer(String id, String name, String platform, int followers) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    @Override
    public int compareTo(Influencer other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Influencer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", followers=" + followers +
                '}';
    }
}