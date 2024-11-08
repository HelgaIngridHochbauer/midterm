package com.Midterm.utils;

import com.Midterm.model.Influencer;
import com.Midterm.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StorageUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    // Save methods
    public static void saveInfluencers(List<Influencer> influencers) throws IOException {
        objectMapper.writeValue(new File("influencers.json"), influencers);
    }

    public static void savePosts(List<Post> posts) throws IOException {
        objectMapper.writeValue(new File("posts.json"), posts);
    }

    public static void saveCampaign(Set<String> campaignNames) {
        try {
            objectMapper.writeValue(new File("campaigns.json"), campaignNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load methods
    public static void loadInfluencers(List<Influencer> influencers) throws IOException {
        File influencersFile = new File("influencers.json");
        if (influencersFile.exists()) {
            List<Influencer> loadedInfluencers = Arrays.asList(objectMapper.readValue(influencersFile, Influencer[].class));
            influencers.clear();
            influencers.addAll(loadedInfluencers);
        }
    }

    public static void loadPosts(List<Post> posts) throws IOException {
        File postsFile = new File("posts.json");
        if (postsFile.exists()) {
            List<Post> loadedPosts = Arrays.asList(objectMapper.readValue(postsFile, Post[].class));
            posts.clear(); // Clear the existing data
            posts.addAll(loadedPosts);
        }
    }


    public static Set<String> loadCampaign() {
        try {
            File file = new File("campaigns.json");
            if (file.exists()) {
                return new HashSet<>(Arrays.asList(objectMapper.readValue(file, String[].class)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>(); // Return an empty set on error
    }

    // Combining save methods
    public static void saveData(List<Influencer> influencers, Map<String, List<Post>> campaigns) throws IOException {
        saveInfluencers(influencers);
        savePosts(campaigns.values().stream().flatMap(List::stream).collect(Collectors.toList())); // Save all posts
        saveCampaign(campaigns.keySet());
    }

    // Combining load methods

    public static void loadData(List<Influencer> influencers, List<Post> posts, Map<String, List<Post>> campaigns) throws IOException {
        influencers.clear(); // Clear existing influencers data
        posts.clear();       // Clear existing posts data
        campaigns.clear();   // Clear existing campaigns data

        loadInfluencers(influencers);
        loadPosts(posts);    // Load posts into the posts list

        // Rebuild the campaigns map from the posts list
        for (Post post : posts) {
            campaigns.computeIfAbsent(post.getCampaignName(), k -> new ArrayList<>()).add(post);
        }

        // Properly merge loaded campaign names into the campaigns map
        Set<String> loadedCampaignNames = loadCampaign();
        for (String campaignName : loadedCampaignNames) {
            campaigns.putIfAbsent(campaignName, new ArrayList<>());
        }
    }

    // Utility to clear storage files

    public static void emptyJsonFiles() throws IOException {
        new File("influencers.json").delete();
        new File("posts.json").delete();
        new File("campaigns.json").delete();
    }
}