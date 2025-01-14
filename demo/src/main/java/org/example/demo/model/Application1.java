
package org.example.demo.model;

import org.example.demo.model.CustomException.InvalidNumberException;
import org.example.demo.utils.InfluencerComparators;
import org.example.demo.utils.InputDevice;
import org.example.demo.utils.OutputDevice;
import org.example.demo.utils.StorageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Application1 {
    private final InputDevice inputDevice;
    private final OutputDevice outputDevice;
    private final PostScheduler postScheduler;
    private final GrowthAnalytics growthAnalytics;
    private int currentFollowers;
    private int previousFollowers;
    private final List<Influencer> influencers;
    private final Map<String, List<Post>> campaigns; // Map of campaign name to list of posts
    private Comparator<Influencer> currentComparator;

    // Format for the date without time
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Application1(InputDevice inputDevice, OutputDevice outputDevice) {
        this.inputDevice = inputDevice;
        this.outputDevice = outputDevice;
        this.postScheduler = new PostScheduler();
        this.growthAnalytics = new GrowthAnalytics(0, 0, 0, 0); // Initialized with default values
        this.currentFollowers = 0;
        this.previousFollowers = 0;
        this.influencers = new ArrayList<>(); // Initialize influencers list
        this.campaigns = new HashMap<>(); // Initialize campaigns map
        this.currentComparator = InfluencerComparators.BY_NAME; // Default sorting by name
        loadData();
    }

    public List<Influencer> getInfluencers() {
        return influencers;
    }
    public Map<String, List<Post>> getCampaigns() {
        return campaigns;
    }
    public void addInfluencer(Influencer influencer) {
        influencers.add(influencer);
    }
    public List<String> getCampaignNames() {
        return new ArrayList<>(campaigns.keySet());
    }
    public int getPreviousFollowers() {
        return previousFollowers;
    }
    public int getCurrentFollowers() {
        return currentFollowers;
    }
    public void setPreviousFollowers(int previousFollowers) {
        this.previousFollowers = previousFollowers;
    }
    public void setCurrentFollowers(int currentFollowers) {
        this.currentFollowers = currentFollowers;
    }

    public List<Post> getCampaignPosts(String campaignName) {
        return campaigns.getOrDefault(campaignName, new ArrayList<>());
    }

    public void schedulePost(Post post, String campaignName) {
        post.setCampaignName(campaignName);
        // Add the post to the internal list
        posts.add(post);
    }
    public List<Post> getPosts() {
        return posts;
    }


    private void newSession() {
        influencers.clear();
        campaigns.clear();
        outputDevice.displayMessage("New session started. All previous data cleared.");
    }

    private void handleError(IOException e) {
        outputDevice.displayMessage("An error occurred: " + e.getMessage());
    }

    public void processArguments(String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "createpost":
                    try {
                        createPost();
                    } catch (InvalidNumberException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "viewcalendar":
                    try {
                        viewCalendar();
                    } catch (InvalidNumberException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "calculatefollowergrowth":
                    calculateFollowerGrowth();
                    break;
                case "compareengagement":
                    try {
                        compareEngagement();
                    } catch (InvalidNumberException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "comparefollowergrowth":
                    try {
                        compareFollowerGrowth();
                    } catch (InvalidNumberException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "addinfluencer":
                    String name = inputDevice.readLine();
                    String platform = inputDevice.readLine();
                    int followers;
                    try {
                        followers = inputDevice.readInt();
                        // Clear the buffer
                    } catch (NumberFormatException | InvalidNumberException e) {
                        outputDevice.displayMessage("Invalid number entered. Please try again.");
                        break;
                    }

                    try {
                        addInfluencer(name, platform, followers);
                    } catch (InvalidNumberException e) {
                        outputDevice.displayMessage("Failed to add influencer: " + e.getMessage());
                    }
                    break;
                case "listinfluencers":
                    listInfluencers();
                    break;
                case "setsortcriterion":
                    try {
                        setSortCriterion();
                    } catch (InvalidNumberException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "groupinfluencersbyplatform":
                    groupInfluencersByPlatform();
                    break;
                case "groupandsortinfluencers":
                    groupAndSortInfluencers();
                    break;
                case "groupinfluencersbycelebritylevel":
                    groupInfluencersByCelebrityLevel();
                    break;
                case "grouppostsbycampaign":
                    groupPostsByCampaign();
                    break;
                default:
                    outputDevice.displayMessage("Unknown command line argument: " + arg);
            }
        }
    }

    public void run() throws IOException {
        try {
            initialMenu();
            boolean running = true;
            while (running) {
                outputDevice.displayMessage("Choose an option:\n" +
                        "0. Dashboard\n" +
                        "1. Create Post\n" +
                        "2. View Calendar\n" +
                        "3. Calculate Follower Growth\n" +
                        "4. Compare Engagement\n" +
                        "5. Compare Follower Growth\n" +
                        "6. Add Influencer\n" +
                        "7. Sort Influencers\n" +
                        "8. Set Sort Criterion\n" +
                        "9. Group Influencers by Platform\n" +
                        "10. Group Influencers by Celebrity Level\n" +
                        "11. Group Posts by Campaign\n" +
                        "12. Exit");

                boolean validInput = false;
                while (!validInput) {
                    try {
                        int choice = inputDevice.readInt();
                        validInput = true;  // valid input received


                        switch (choice) {
                            case 0:
                                interactiveDashboard();
                                break;
                            case 1:
                                createPost();
                                break;
                            case 2:
                                viewCalendar();
                                break;
                            case 3:
                                calculateFollowerGrowth();
                                break;
                            case 4:
                                compareEngagement();
                                break;
                            case 5:
                                compareFollowerGrowth();
                                break;
                            case 6:

                                String name = inputDevice.readLine();


                                String platform = inputDevice.readLine();


                                int followers;
                                try {
                                    followers = inputDevice.readInt();
                                    // Clear the buffer
                                } catch (NumberFormatException e) {
                                    outputDevice.displayMessage("Invalid number entered. Please try again.");
                                    break;
                                }

                                try {
                                    addInfluencer(name, platform, followers);
                                } catch (InvalidNumberException e) {
                                    outputDevice.displayMessage("Failed to add influencer: " + e.getMessage());
                                }
                                break;
                            case 7:
                                listInfluencers();
                                break;
                            case 8:
                                setSortCriterion();
                                break;
                            case 9:
                                groupAndSortInfluencers();
                                break;
                            case 10:
                                groupInfluencersByCelebrityLevel();
                                break;
                            case 11:
                                groupPostsByCampaign();
                                break;
                            case 12:
                                saveData();
                                System.exit(0);
                                break;
                            default:
                                outputDevice.displayMessage("Invalid choice. Please try again.");
                        }
                        inputDevice.readLine();
                    } catch (InvalidNumberException e) {
                        outputDevice.displayMessage("An invalid number was entered: " + e.getMessage());
                    }
                }
            }
            saveData();
        } catch (IOException e) {
            outputDevice.displayMessage("An error occurred: " + e.getMessage());
        }
    }

    private void initialMenu() throws IOException {
        outputDevice.displayMessage("Welcome to the Application!");
        outputDevice.displayMessage("Do you want to continue the previous session or start a new one?");
        outputDevice.displayMessage("1. Continue previous session");
        outputDevice.displayMessage("2. Start a new session (WARNING: All data will be lost)");

        int choice = -1;  // Initialize to invalid choice value
        while (choice != 1 && choice != 2) {
            try {
                choice = inputDevice.readInt();
                if (choice != 1 && choice != 2) {
                    outputDevice.displayMessage("Invalid choice. Please enter 1 or 2.");
                }

            } catch (InvalidNumberException e) {
                outputDevice.displayMessage(e.getMessage()); // Prompt user again
            }

        }

        if (choice == 1) {
            loadData();
        } else {
            newSession();
        }
    }

    private void saveData() {
        StorageUtils.saveData(influencers, campaigns);
        outputDevice.displayMessage("Data saved successfully.");
    }

    List<Post> posts = new ArrayList<>();

    private void loadData() {
        StorageUtils.loadData(influencers, posts, campaigns);
    }



    public void groupPostsByCampaign() {
        outputDevice.displayMessage("Posts grouped by campaign:");
        for (Map.Entry<String, List<Post>> entry : campaigns.entrySet()) {
            outputDevice.displayMessage("Campaign: " + entry.getKey());
            for (Post post : entry.getValue()) {
                Influencer influencer = post.getInfluencer();
                String influencerName = (influencer != null) ? influencer.getName() : "Unknown Influencer";
                outputDevice.displayMessage(
                        String.format("  Post: %s by %s on %s",
                                post.getContent(),
                                influencerName,
                                dateFormat.format(post.getDate())  // Format the date without time
                        )
                );
            }
        }
    }

    public String getGroupedPostsByCampaign() {
        if (campaigns == null || campaigns.isEmpty()) {
            return "No campaigns found.";
        }

        StringBuilder result = new StringBuilder("Posts grouped by campaign:\n");

        for (Map.Entry<String, List<Post>> entry : campaigns.entrySet()) {
            result.append("Campaign: ").append(entry.getKey()).append("\n"); // Campaign name
            for (Post post : entry.getValue()) {
                Influencer influencer = post.getInfluencer();
                String influencerName = (influencer != null) ? influencer.getName() : "Unknown Influencer";
                result.append(
                        String.format("  Post: %s by %s on %s%n",
                                post.getContent(),
                                influencerName,
                                dateFormat.format(post.getDate())  // Format the date
                        )
                );
            }
        }

        return result.toString();
    }

    public void viewCalendar() throws InvalidNumberException {
        // Load the latest data
        loadData();

        try {
            outputDevice.displayMessage("Enter month (e.g., 10 for October): ");
            int month = Integer.parseInt(inputDevice.readLine().trim());
            outputDevice.displayMessage("Enter year : ");
            int year = Integer.parseInt(inputDevice.readLine().trim());

            if (month < 1 || month > 12) {
                throw new CustomException.InvalidNumberException("Month must be between 1 and 12.");
            }

            if (posts == null) {
                throw new NullPointerException("Posts list is not initialized.");
            }

            List<Post> filteredPosts = posts.stream()
                    .filter(post -> {
                        LocalDate postDate = post.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return postDate.getMonthValue() == month && postDate.getYear() == year;
                    })
                    .collect(Collectors.toList());

            if (filteredPosts == null) {
                throw new NullPointerException("Filtered posts list is not initialized.");
            }

            if (filteredPosts.isEmpty()) {
                outputDevice.displayMessage("No posts scheduled for this month.");
            } else {
                for (Post post : filteredPosts) {
                    outputDevice.displayMessage("Post: " + post.getContent() + " by " + post.getInfluencer().getName() + " on " + dateFormat.format(post.getDate()));
                }
            }
        } catch (NumberFormatException e) {
            outputDevice.displayMessage("Invalid input format. Please enter valid numeric values for month and year: " + e.getMessage());
        } catch (NullPointerException e) {
            outputDevice.displayMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void calculateFollowerGrowth() {
        try {
            outputDevice.displayMessage("Enter current followers: ");
            currentFollowers = inputDevice.readInt();
            inputDevice.readLine();  // Clear buffer

            outputDevice.displayMessage("Enter previous followers: ");
            previousFollowers = inputDevice.readInt();

            double growth = growthAnalytics.analyzeFollowerGrowth(currentFollowers, previousFollowers);
            outputDevice.displayMessage("Follower Growth: " + growth + "%");
        } catch (InvalidNumberException e) {
            outputDevice.displayMessage("An invalid number was entered: " + e.getMessage());
        }
    }

    public void compareEngagement() throws InvalidNumberException {
        outputDevice.displayMessage("Enter current likes for GA1: ");
        int currentLikes1 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        outputDevice.displayMessage("Enter comments for GA1: ");
        int comments1 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        outputDevice.displayMessage("Enter current likes for GA2: ");
        int currentLikes2 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        outputDevice.displayMessage("Enter comments for GA2: ");
        int comments2 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        GrowthAnalytics ga1 = new GrowthAnalytics(currentLikes1, comments1, 0, 0);
        GrowthAnalytics ga2 = new GrowthAnalytics(currentLikes2, comments2, 0, 0);

        int comparisonResult = ga1.compareTo(ga2);

        if (comparisonResult < 0) {
            outputDevice.displayMessage("GA1 has less engagement than GA2.");
        } else if (comparisonResult > 0) {
            outputDevice.displayMessage("GA1 has more engagement than GA2.");
        } else {
            outputDevice.displayMessage("GA1 and GA2 have the same engagement.");
        }
    }

    public void compareFollowerGrowth() throws InvalidNumberException {
        outputDevice.displayMessage("Enter current followers for GA1: ");
        int currentFollowers1 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        outputDevice.displayMessage("Enter previous followers for GA1: ");
        int previousFollowers1 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        outputDevice.displayMessage("Enter current followers for GA2: ");
        int currentFollowers2 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        outputDevice.displayMessage("Enter previous followers for GA2: ");
        int previousFollowers2 = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        GrowthAnalytics ga1 = new GrowthAnalytics(0, 0, currentFollowers1, previousFollowers1);
        GrowthAnalytics ga2 = new GrowthAnalytics(0, 0, currentFollowers2, previousFollowers2);

        int comparisonResult = ga1.compareTo(ga2);

        if (comparisonResult < 0) {
            outputDevice.displayMessage("GA1 has less follower growth than GA2.");
        } else if (comparisonResult > 0) {
            outputDevice.displayMessage("GA1 has more follower growth than GA2.");
        } else {
            outputDevice.displayMessage("GA1 and GA2 have the same follower growth.");
        }
    }



    public String listInfluencers() {
        StringBuilder result = new StringBuilder();

        // Sort the influencers
        Collections.sort(influencers, currentComparator);

        if (influencers.isEmpty()) {
            String message = "No influencers to display.";
            outputDevice.displayMessage(message); // Send to the output device
            result.append(message);              // Append to the output for UI
        } else {
            String message = "Influencers:";
            outputDevice.displayMessage(message); // Send to the output device
            result.append(message).append("\n");  // Append to the output for UI

            for (Influencer influencer : influencers) {
                String influencerMessage = String.format(
                        "Name: %s, Platform: %s, Followers: %d",
                        influencer.getName(),
                        influencer.getPlatform().isEmpty() ? "N/A" : influencer.getPlatform(),
                        influencer.getFollowers()
                );
                outputDevice.displayMessage(influencerMessage); // Send to the output device
                result.append(influencerMessage).append("\n");  // Append to the output for UI
            }
        }

        return result.toString(); // Return the full influencers' details as a single String
    }

    public void setSortCriterion() throws InvalidNumberException {
        outputDevice.displayMessage("Choose a sort criterion:\n" +
                "1. Sort by Name\n" +
                "2. Sort Descendingly by Number of Followers\n" +
                "3. Sort Ascendingly by Number of Followers\n");
        int choice = inputDevice.readInt();


        switch (choice) {
            case 1:
                currentComparator = InfluencerComparators.BY_NAME;
                break;
            case 2:
                currentComparator = InfluencerComparators.BY_FOLLOWERS_DESCENDING;
                break;
            case 3:
                currentComparator = InfluencerComparators.BY_FOLLOWERS_ASCENDING;
                break;
            default:
                outputDevice.displayMessage("Invalid choice. Keeping previous sort criterion.");
        }

        outputDevice.displayMessage("Sort criterion updated successfully.");
    }

    public void updateSortCriterion(int choice) throws InvalidNumberException {
        switch (choice) {
            case 1:
                currentComparator = InfluencerComparators.BY_NAME;
                break;
            case 2:
                currentComparator = InfluencerComparators.BY_FOLLOWERS_DESCENDING;
                break;
            case 3:
                currentComparator = InfluencerComparators.BY_FOLLOWERS_ASCENDING;
                break;
            default:
                throw new InvalidNumberException("Invalid choice for sorting criterion.");
        }
    }

    public void groupInfluencersByPlatform() {
        Map<String, List<Influencer>> groupedByPlatform = influencers.stream()
                .collect(Collectors.groupingBy(Influencer::getPlatform));

        outputDevice.displayMessage("Influencers grouped by platform:");
        for (Map.Entry<String, List<Influencer>> entry : groupedByPlatform.entrySet()) {
            outputDevice.displayMessage("Platform: " + (entry.getKey().isEmpty() ? "N/A" : entry.getKey()));
            for (Influencer influencer : entry.getValue()) {
                outputDevice.displayMessage(
                        String.format("  Name: %s, Followers: %d",
                                influencer.getName(),
                                influencer.getFollowers()
                        )
                );
            }
        }
    }

    public String getGroupedInfluencersByPlatform() {
        if (influencers == null || influencers.isEmpty()) {
            return "No influencers found.";
        }

        Map<String, List<Influencer>> groupedByPlatform = influencers.stream()
                .collect(Collectors.groupingBy(Influencer::getPlatform));

        StringBuilder result = new StringBuilder("Influencers grouped by platform:\n");
        for (Map.Entry<String, List<Influencer>> entry : groupedByPlatform.entrySet()) {
            result.append("Platform: ").append(entry.getKey().isEmpty() ? "N/A" : entry.getKey()).append("\n");
            for (Influencer influencer : entry.getValue()) {
                result.append(String.format("  Name: %s, Followers: %d%n",
                        influencer.getName(),
                        influencer.getFollowers()
                ));
            }
        }
        return result.toString();
    }

    private void groupAndSortInfluencers() {
        // Group by platform first
        Map<String, List<Influencer>> groupedByPlatform = influencers.stream()
                .collect(Collectors.groupingBy(Influencer::getPlatform));

        outputDevice.displayMessage("Influencers grouped and sorted by platform (ascending followers):");

        for (Map.Entry<String, List<Influencer>> entry : groupedByPlatform.entrySet()) {
            List<Influencer> sortedList = entry.getValue().stream()
                    .sorted(InfluencerComparators.BY_FOLLOWERS_ASCENDING)
                    .collect(Collectors.toList());

            outputDevice.displayMessage("Platform: " + (entry.getKey().isEmpty() ? "N/A" : entry.getKey()));
            for (Influencer influencer : sortedList) {
                outputDevice.displayMessage(
                        String.format("  Name: %s, Followers: %d",
                                influencer.getName(),
                                influencer.getFollowers()
                        )
                );
            }
        }
    }

    public void groupInfluencersByCelebrityLevel() {
        Map<String, List<Influencer>> celebrityLevels = new HashMap<>();
        celebrityLevels.put("A-list", new ArrayList<>());
        celebrityLevels.put("B-list", new ArrayList<>());
        celebrityLevels.put("C-list", new ArrayList<>());

        for (Influencer influencer : influencers) {
            if (influencer.getFollowers() > 100000) {
                celebrityLevels.get("A-list").add(influencer);
            } else if (influencer.getFollowers() > 10000) {
                celebrityLevels.get("B-list").add(influencer);
            } else {
                celebrityLevels.get("C-list").add(influencer);
            }
        }

        // Use a predetermined order to display the groups
        List<String> order = Arrays.asList("A-list", "B-list", "C-list");

        outputDevice.displayMessage("Influencers grouped by celebrity level:");

        for (String key : order) {
            outputDevice.displayMessage(key + ":");
            for (Influencer influencer : celebrityLevels.get(key)) {
                outputDevice.displayMessage(
                        String.format("  Name: %s, Platform: %s, Followers: %d",
                                influencer.getName(),
                                influencer.getPlatform().isEmpty() ? "N/A" : influencer.getPlatform(),
                                influencer.getFollowers()
                        )
                );
            }
        }
    }

    public String getGroupedInfluencersByCelebrityLevel() {
        if (influencers == null || influencers.isEmpty()) {
            return "No influencers found.";
        }

        Map<String, List<Influencer>> celebrityLevels = new HashMap<>();
        celebrityLevels.put("A-list", new ArrayList<>());
        celebrityLevels.put("B-list", new ArrayList<>());
        celebrityLevels.put("C-list", new ArrayList<>());

        // Group influencers by celebrity level
        for (Influencer influencer : influencers) {
            if (influencer.getFollowers() > 100000) {
                celebrityLevels.get("A-list").add(influencer);
            } else if (influencer.getFollowers() > 10000) {
                celebrityLevels.get("B-list").add(influencer);
            } else {
                celebrityLevels.get("C-list").add(influencer);
            }
        }

        // Build a string representation of the results
        StringBuilder result = new StringBuilder("Influencers grouped by celebrity level:\n");
        List<String> order = Arrays.asList("A-list", "B-list", "C-list");
        for (String level : order) {
            result.append(level).append(":\n");
            for (Influencer influencer : celebrityLevels.get(level)) {
                result.append(String.format("  Name: %s, Platform: %s, Followers: %d%n",
                        influencer.getName(),
                        influencer.getPlatform().isEmpty() ? "N/A" : influencer.getPlatform(),
                        influencer.getFollowers()
                ));
            }
        }

        return result.toString();
    }


    // Method to delete a campaign
    public void deleteCampaign(String campaignName) {
        if (campaigns.remove(campaignName) != null) {
            outputDevice.displayMessage("Campaign deleted successfully.");
        } else {
            outputDevice.displayMessage("Campaign not found.");
        }
    }


    public void editInfluencerDetails(Influencer influencer) {
        outputDevice.displayMessage("Current details of the influencer:");
        outputDevice.displayMessage("Name: " + influencer.getName() + ", Platform: " + influencer.getPlatform() + ", Followers: " + influencer.getFollowers());

        // Ask if the user wants to change the name
        outputDevice.displayMessage("Do you want to change the name? (yes/no)");
        if (inputDevice.readLine().trim().equalsIgnoreCase("yes")) {
            outputDevice.displayMessage("Enter new name:");
            String newName = inputDevice.readLine().trim();
            influencer.setName(newName);
        }

        // Ask if the user wants to change the platform
        outputDevice.displayMessage("Do you want to change the platform? (yes/no)");
        if (inputDevice.readLine().trim().equalsIgnoreCase("yes")) {
            outputDevice.displayMessage("Enter new platform:");
            String newPlatform = inputDevice.readLine().trim();
            influencer.setPlatform(newPlatform);
        }

        // Ask if the user wants to change the followers
        outputDevice.displayMessage("Do you want to change the follower count? (yes/no)");
        if (inputDevice.readLine().trim().equalsIgnoreCase("yes")) {
            outputDevice.displayMessage("Enter new follower count:");
            String newFollowersInput = inputDevice.readLine().trim();
            try {
                int newFollowers = Integer.parseInt(newFollowersInput);
                influencer.setFollowers(newFollowers);
            } catch (NumberFormatException e) {
                outputDevice.displayMessage("Invalid number format for followers. Keeping the current follower count.");
            }
        }

        outputDevice.displayMessage("Influencer details updated successfully.");
    }

    public void interactiveDashboard() throws IOException, InvalidNumberException {
        // Display Influencers
        displayInfluencers();
        outputDevice.displayMessage("Enter the name of the influencer you wish to manage:");
        String influencerName = inputDevice.readLine();
        Influencer selectedInfluencer = null;

        // Find selected influencer
        for (Influencer influencer : influencers) {
            if (influencer.getName().equalsIgnoreCase(influencerName)) {
                selectedInfluencer = influencer;
                break;
            }
        }

        // Check if influencer is found
        if (selectedInfluencer == null) {
            outputDevice.displayMessage("Influencer not found.");
            return;
        }

        // Show dashboard options for the selected influencer
        while (true) {
            outputDevice.displayMessage("Dashboard Options for " + selectedInfluencer.getName() + ":");
            outputDevice.displayMessage("1. View Campaigns and Posts");
            outputDevice.displayMessage("2. Edit Post");
            outputDevice.displayMessage("3. Edit Influencer Details");
            outputDevice.displayMessage("4. View Influencer Details");
            outputDevice.displayMessage("5. Exit Dashboard");

            int choice = -1;
            while (choice == -1) {
                try {
                    choice = inputDevice.readInt(); // Try reading integer
                } catch (InvalidNumberException e) {
                    outputDevice.displayMessage(e.getMessage()); // Display error message
                }
            }

            switch (choice) {
                case 1:
                    displayCampaignsAndPostsForInfluencer(selectedInfluencer);
                    break;
                case 2:
                    displayCampaignsAndPostsForInfluencer(selectedInfluencer);
                    outputDevice.displayMessage("Enter the post ID:");
                    String postId = inputDevice.readLine(); // Read the post ID from user input
                    editPostForInfluencer(selectedInfluencer, postId); // Edit the post
                    break;
                case 3:

                    editInfluencerDetails(selectedInfluencer);
                    break;
                case 4:
                    viewInfluencerDetails(selectedInfluencer);
                    break;
                case 5:
                    return;
                default:
                    outputDevice.displayMessage("Invalid option. Please try again.");
            }
        }
    }


    private void viewInfluencerDetails(Influencer influencer) {
        outputDevice.displayMessage("Influencer Details:");
        outputDevice.displayMessage("====================");
        outputDevice.displayMessage(String.format(
                "Name         : %s\n" +
                        "Platform     : %s\n" +
                        "Followers    : %d\n" +
                        "ID           : %s",
                influencer.getName(),
                influencer.getPlatform().isEmpty() ? "N/A" : influencer.getPlatform(),
                influencer.getFollowers(),
                influencer.getId()
        ));
        outputDevice.displayMessage("====================");

        boolean influencerHasPosts = false;
        List<Post> influencerPosts = new ArrayList<>();

        for (Map.Entry<String, List<Post>> entry : campaigns.entrySet()) {
            List<Post> posts = entry.getValue();

            influencerPosts.addAll(
                    posts.stream()
                            .filter(post -> post.getInfluencerId().equals(influencer.getId()))
                            .collect(Collectors.toList())
            );
        }

        if (!influencerPosts.isEmpty()) {
            influencerHasPosts = true;
            outputDevice.displayMessage("Posts:");
            outputDevice.displayMessage("------");
            for (Post post : influencerPosts) {
                outputDevice.displayMessage(String.format(
                        "ID       : %s\n" +
                                "Date     : %s\n" +
                                "Content  : %s\n" +
                                "Campaign : %s\n" +
                                "Likes    : %d (current) / %d (previous) / %d (expected)\n" +
                                "Comments : %d",
                        post.getId(),
                        dateFormat.format(post.getDate()),
                        post.getContent(),
                        post.getCampaignName(),
                        post.getCurrentLikes(),
                        post.getPreviousLikes(),
                        post.getExpectedLikes(),
                        post.getComments()
                ));
                outputDevice.displayMessage("------");
            }
        }

        if (!influencerHasPosts) {
            outputDevice.displayMessage("No posts found for this influencer.");
        }
    }
    private void displayInfluencers() {
        if (influencers.isEmpty()) {
            outputDevice.displayMessage("No influencers available.");
        } else {
            outputDevice.displayMessage("Available Influencers:");
            for (Influencer influencer : influencers) {
                outputDevice.displayMessage(
                        String.format("Name: %s, Platform: %s, Followers: %d",
                                influencer.getName(),
                                influencer.getPlatform().isEmpty() ? "N/A" : influencer.getPlatform(),
                                influencer.getFollowers()
                        )
                );
            }
        }
    }
    public void displayAllPostTitles() {
        if (campaigns.isEmpty()) {
            outputDevice.displayMessage("No campaigns available.");
            return;
        }

        outputDevice.displayMessage("All Post Titles:");
        outputDevice.displayMessage("================");

        for (Map.Entry<String, List<Post>> entry : campaigns.entrySet()) {
            String campaignName = entry.getKey();
            List<Post> posts = entry.getValue();

            outputDevice.displayMessage(String.format("Campaign: %s", campaignName));
            for (Post post : posts) {
                outputDevice.displayMessage(String.format("  - %s", post.getContent()));
            }
        }
    }

    private void displayCampaignsAndPostsForInfluencer(Influencer influencer) {
        boolean influencerHasPosts = false;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map.Entry<String, List<Post>> entry : campaigns.entrySet()) {
            String campaignName = entry.getKey();
            List<Post> posts = entry.getValue();

            List<Post> influencerPosts = posts.stream()
                    .filter(post -> post.getInfluencerId().equals(influencer.getId()))
                    .collect(Collectors.toList());

            if (!influencerPosts.isEmpty()) {
                influencerHasPosts = true;
                outputDevice.displayMessage("Campaign: " + campaignName);
                outputDevice.displayMessage("Posts:");
                outputDevice.displayMessage("------");

                for (Post post : influencerPosts) {
                    outputDevice.displayMessage(String.format(
                            "ID       : %s\n" +
                                    "Date     : %s\n" +
                                    "Content  : %s\n" +
                                    "Campaign : %s\n" +
                                    "Likes    : %d (current) / %d (previous) / %d (expected)\n" +
                                    "Comments : %d",
                            post.getId(),
                            dateFormat.format(convertToLocalDateViaInstant(post.getDate())),
                            post.getContent(),
                            post.getCampaignName(),
                            post.getCurrentLikes(),
                            post.getPreviousLikes(),
                            post.getExpectedLikes(),
                            post.getComments())
                    );
                    outputDevice.displayMessage("------");
                }
            }
        }

        if (!influencerHasPosts) {
            outputDevice.displayMessage("No posts found for the influencer.");
        }
    }

    private void editPostForInfluencer(Influencer influencer, String postId) throws IOException, InvalidNumberException {
        boolean postFound = false;

        for (Map.Entry<String, List<Post>> entry : campaigns.entrySet()) {
            List<Post> posts = entry.getValue();

            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);
                if (post.getId().equals(postId) && post.getInfluencerId().equals(influencer.getId())) {
                    postFound = true;

                    outputDevice.displayMessage("Do you want to modify the content? (yes/no)");
                    String modifyContent = inputDevice.readLine().trim().toLowerCase();
                    if (modifyContent.equals("yes")) {
                        outputDevice.displayMessage("Enter new content: ");
                        String newContent = inputDevice.readLine();
                        post.setContent(newContent);
                    }

                    outputDevice.displayMessage("Do you want to modify the number of likes? (yes/no)");
                    String modifyLikes = inputDevice.readLine().trim().toLowerCase();
                    if (modifyLikes.equals("yes")) {
                        outputDevice.displayMessage("Enter new number of likes: ");
                        int newLikes = inputDevice.readInt();
                        inputDevice.readLine(); // Clear buffer
                        post.setPreviousLikes(post.getCurrentLikes()); // Set previous likes to the current likes
                        post.setCurrentLikes(newLikes); // Update current likes
                    }

                    outputDevice.displayMessage("Do you want to modify the number of comments? (yes/no)");
                    String modifyComments = inputDevice.readLine().trim().toLowerCase();
                    if (modifyComments.equals("yes")) {
                        outputDevice.displayMessage("Enter new number of comments: ");
                        int newComments = inputDevice.readInt();
                        inputDevice.readLine(); // Clear buffer
                        post.setComments(newComments);
                    }

                    // Update the list with the modified post
                    posts.set(i, post);
                    outputDevice.displayMessage("Post edited successfully.");

                    // Save updated campaigns back to JSON
                    // Save updated campaigns back to the database using the public saveData method
                    List<Influencer> influencers = new ArrayList<>(); // Load your influencers here if needed
                    StorageUtils.saveData(influencers, campaigns);

                    return;
                }
            }
        }

        if (!postFound) {
            outputDevice.displayMessage("Post not found for influencer " + influencer.getName() + ".");
        }
    }


    private boolean isDateValid(LocalDate date) {
        LocalDate today = LocalDate.now();
        return !date.isBefore(today);
    }

        private boolean isInfluencerNameUnique(String name) {
            for (Influencer influencer : influencers) {
                if (influencer.getName().equalsIgnoreCase(name)) {
                    return false;
                }
            }
            return true;
        }



    public void createPost() throws CustomException.InvalidNumberException {
        if (influencers.isEmpty()) {
            outputDevice.displayMessage("No influencers available. Please add an influencer first.");
            return;
        }

        // List available influencers
        outputDevice.displayMessage("Select an influencer by entering the corresponding number:");
        for (int i = 0; i < influencers.size(); i++) {
            Influencer influencer = influencers.get(i);
            outputDevice.displayMessage((i + 1) + ". " + influencer.getName() + " (" + influencer.getPlatform() + ")");
        }

        int influencerIndex = inputDevice.readInt() - 1;
        inputDevice.readLine();  // Clear buffer

        if (influencerIndex < 0 || influencerIndex >= influencers.size()) {
            outputDevice.displayMessage("Invalid selection. Post not created.");
            return;
        }

        Influencer selectedInfluencer = influencers.get(influencerIndex);

        try {
            outputDevice.displayMessage("Enter content of the post: ");
            String content = inputDevice.readLine();
            outputDevice.displayMessage("Enter expected likes: ");
            int expectedLikes = inputDevice.readInt();
            inputDevice.readLine();  // Clear buffer

            outputDevice.displayMessage("Enter expected comments: ");
            int expectedComments = inputDevice.readInt();
            inputDevice.readLine();  // Clear buffer

            outputDevice.displayMessage("Enter post date (yyyy-MM-dd): ");
            String dateString = inputDevice.readLine();

            outputDevice.displayMessage("Do you want this post to be part of an existing campaign? (yes/no)");
            String response = inputDevice.readLine().trim().toLowerCase();
            String campaignName = "";

            if (response.equals("yes")) {
                // List available campaigns
                if (campaigns.isEmpty()) {
                    outputDevice.displayMessage("No existing campaigns. You need to create a new one.");
                } else {
                    outputDevice.displayMessage("Select a campaign by entering the corresponding number:");
                    int index = 1;
                    for (String campaign : campaigns.keySet()) {
                        outputDevice.displayMessage(index + ". " + campaign);
                        index++;
                    }

                    int campaignIndex = inputDevice.readInt() - 1;
                    inputDevice.readLine();  // Clear buffer

                    if (campaignIndex < 0 || campaignIndex >= campaigns.size()) {
                        outputDevice.displayMessage("Invalid selection. Post not created.");
                        return;
                    }

                    campaignName = new ArrayList<>(campaigns.keySet()).get(campaignIndex);
                }
            } else {
                outputDevice.displayMessage("Enter campaign name: ");
                campaignName = inputDevice.readLine();
            }

            // Parse and validate the date
            LocalDate postDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            postDate = LocalDate.parse(dateString, formatter);

            if (!isDateValid(postDate)) {
                outputDevice.displayMessage("The date must not be in the past. Post not created.");
                return;
            }

            // Create a Post object using the provided inputs and associate it with the selected influencer
            Post post = new Post(content, java.sql.Date.valueOf(postDate), expectedLikes, 0, 0, 0, currentFollowers, previousFollowers, campaignName);
            post.setExpectedComments(expectedComments);
            post.setInfluencer(selectedInfluencer);
            postScheduler.schedulePost(post);

            campaigns.computeIfAbsent(campaignName, k -> new ArrayList<>()).add(post); // Add post to the campaign

            outputDevice.displayMessage("Post scheduled successfully.");
            saveData();
        } catch (CustomException.PostCreationException e) {
            outputDevice.displayMessage("Incorrect input: " + e.getMessage());
        } catch (DateTimeParseException e) {
            outputDevice.displayMessage("Invalid date format or invalid date. Post not created.");
        }
    }
    private int parseNumber(String input, String field) throws CustomException.InvalidNumberException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new CustomException.InvalidNumberException("Invalid input for " + field + ": " + input);
        }
    }

    public void addInfluencer(String name, String platform, int followers) throws InvalidNumberException {
        if (!isInfluencerNameUnique(name)) {
            outputDevice.displayMessage("An influencer with this name already exists. Influencer not added.");
            return;
        }

        String id = UUID.randomUUID().toString(); // Generate unique ID
        Influencer influencer = new Influencer(id, name, platform, followers);
        influencers.add(influencer);

        outputDevice.displayMessage("Influencer added successfully.");
        saveData();
    }


    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert instanceof java.sql.Date) {
            // Convert java.sql.Date to java.util.Date
            dateToConvert = new Date(dateToConvert.getTime());
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


//to remember: de fiecare data cand folosesti o functie care creaza tr sa adaugi un savedata()
    //fiindca nu mai ai main loop-ul ala in care se salvau modificarile

}