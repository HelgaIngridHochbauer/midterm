package org.example.demo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String content;
    private Date date;
    private int expectedLikes;
    private int currentLikes;
    private int previousLikes;
    private int comments;
    private int currentFollowers;
    private int previousFollowers;
    private String campaignName;
    private String influencerId;
    private transient Influencer influencer; // This field will not be serialized
    private int expectedComments;

    // Default constructor
    public Post() {
        this.id = UUID.randomUUID().toString(); // Generate a unique ID
    }

    // Parameterized constructor
    public Post(String content, Date date, int expectedLikes, int currentLikes, int previousLikes, int comments, int currentFollowers, int previousFollowers, String campaignName) throws CustomException.PostCreationException, CustomException.InvalidNumberException {
        this(); // Call the default constructor to initialize the ID
        if (content == null || content.isBlank()) {
            throw new CustomException.PostCreationException("Content cannot be null or empty.");
        }
        if (expectedLikes < 0 || currentLikes < 0 || previousLikes < 0 || comments < 0 || currentFollowers < 0 || previousFollowers < 0) {
            throw new CustomException.InvalidNumberException("Number values cannot be negative.");
        }
        this.content = content;
        this.date = date;
        this.expectedLikes = expectedLikes;
        this.currentLikes = currentLikes;
        this.previousLikes = previousLikes;
        this.comments = comments;
        this.currentFollowers = currentFollowers;
        this.previousFollowers = previousFollowers;
        this.campaignName = campaignName;
        this.influencerId = influencerId;
        this.expectedComments = 0; // Initialize as 0 if not provided
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getExpectedLikes() {
        return expectedLikes;
    }

    public void setExpectedLikes(int expectedLikes) throws CustomException.InvalidNumberException {
        if (expectedLikes < 0) {
            throw new CustomException.InvalidNumberException("Expected likes cannot be negative.");
        }
        this.expectedLikes = expectedLikes;
    }

    public int getCurrentLikes() {
        return currentLikes;
    }

    public void setCurrentLikes(int currentLikes) throws CustomException.InvalidNumberException {
        if (currentLikes < 0) {
            throw new CustomException.InvalidNumberException("Current likes cannot be negative.");
        }
        this.currentLikes = currentLikes;
    }

    public int getPreviousLikes() {
        return previousLikes;
    }

    public void setPreviousLikes(int previousLikes) throws CustomException.InvalidNumberException {
        if (previousLikes < 0) {
            throw new CustomException.InvalidNumberException("Previous likes cannot be negative.");
        }
        this.previousLikes = previousLikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) throws CustomException.InvalidNumberException {
        if (comments < 0) {
            throw new CustomException.InvalidNumberException("Comments cannot be negative.");
        }
        this.comments = comments;
    }

    public int getCurrentFollowers() {
        return currentFollowers;
    }

    public void setCurrentFollowers(int currentFollowers) throws CustomException.InvalidNumberException {
        if (currentFollowers < 0) {
            throw new CustomException.InvalidNumberException("Current followers cannot be negative.");
        }
        this.currentFollowers = currentFollowers;
    }

    public int getPreviousFollowers() {
        return previousFollowers;
    }

    public void setPreviousFollowers(int previousFollowers) throws CustomException.InvalidNumberException {
        if (previousFollowers < 0) {
            throw new CustomException.InvalidNumberException("Previous followers cannot be negative.");
        }
        this.previousFollowers = previousFollowers;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getInfluencerId() {
        return influencerId != null ? influencerId : (influencer != null ? influencer.getId() : null);
    }

    public void setInfluencer(Influencer influencer) {
        this.influencer = influencer;
        this.influencerId = influencer != null ? influencer.getId() : null;
    }

    public Influencer getInfluencer() {
        return influencer;
    }

    public int getExpectedComments() {
        return expectedComments;
    }

    public void setExpectedComments(int expectedComments) {
        this.expectedComments = expectedComments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", expectedLikes=" + expectedLikes +
                ", currentLikes=" + currentLikes +
                ", previousLikes=" + previousLikes +
                ", comments=" + comments +
                ", currentFollowers=" + currentFollowers +
                ", previousFollowers=" + previousFollowers +
                ", campaignName='" + campaignName + '\'' +
                ", influencerId='" + influencerId + '\'' +
                ", expectedComments=" + expectedComments +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }
}