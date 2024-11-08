
package com.Midterm.model;

import com.Midterm.utils.InfluencerComparators;
import com.Midterm.utils.InputDevice;
import com.Midterm.utils.OutputDevice;
import com.Midterm.utils.StorageUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.Midterm.model.CustomException.InvalidNumberException;
import java.util.Date;
import java.util.InputMismatchException;

import java.util.*;
import java.util.stream.Collectors;

public class Application {
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

    public Application(InputDevice inputDevice, OutputDevice outputDevice) {
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
                    try {
                        addInfluencer();
                    } catch (InvalidNumberException e) {
                        throw new RuntimeException(e);
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
                                addInfluencer();
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
                inputDevice.readLine(); // Clear buffer
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
        try {
            StorageUtils.saveData(influencers, campaigns);
            outputDevice.displayMessage("Data saved successfully.");
        } catch (IOException e) {
            outputDevice.displayMessage("Error saving data: " + e.getMessage());
            e.printStackTrace(); // For debugging purposes
        }
    }

    List<Post> posts = new ArrayList<>();

    private void loadData() {
        try {
            StorageUtils.loadData(influencers, posts, campaigns);
        } catch (IOException e) {
            outputDevice.displayMessage("An error occurred while loading data: " + e.getMessage());
            e.printStackTrace();  // For debugging purposes
        }
    }



    private void groupPostsByCampaign() {
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

    private void viewCalendar() throws InvalidNumberException {
        outputDevice.displayMessage("Enter month and year (e.g., 10 2024 for October 2024): ");
        int month = inputDevice.readInt();
        int year = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

        List<Post> posts = postScheduler.getPostsByMonth(month, year);
        if (posts.isEmpty()) {
            outputDevice.displayMessage("No posts scheduled for this month.");
        } else {
            for (Post post : posts) {
                outputDevice.displayMessage("Post: " + post.getContent() + " by " + post.getInfluencer().getName() + " on " + dateFormat.format(post.getDate()));
            }
        }
    }

    private void calculateFollowerGrowth() {
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

    private void compareEngagement() throws InvalidNumberException {
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

    private void compareFollowerGrowth() throws InvalidNumberException {
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



    private void listInfluencers() {
        Collections.sort(influencers, currentComparator);

        if (influencers.isEmpty()) {
            outputDevice.displayMessage("No influencers to display.");
        } else {
            outputDevice.displayMessage("Influencers:");
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

    private void setSortCriterion() throws InvalidNumberException {
        outputDevice.displayMessage("Choose a sort criterion:\n" +
                "1. Sort by Name\n" +
                "2. Sort Descendingly by Number of Followers\n" +
                "3. Sort Ascendingly by Number of Followers\n");
        int choice = inputDevice.readInt();
        inputDevice.readLine();  // Clear buffer

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

    private void groupInfluencersByPlatform() {
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

    private void groupInfluencersByCelebrityLevel() {
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


    // Method to delete a campaign
    public void deleteCampaign(String campaignName) {
        if (campaigns.remove(campaignName) != null) {
            outputDevice.displayMessage("Campaign deleted successfully.");
        } else {
            outputDevice.displayMessage("Campaign not found.");
        }
    }


    public void editInfluencerDetails(String influencerName, int newFollowers) {
        for (Influencer influencer : influencers) {
            if (influencer.getName().equals(influencerName)) {
                influencer.setFollowers(newFollowers);
                outputDevice.displayMessage("Influencer details updated successfully.");
                return;
            }
        }
        outputDevice.displayMessage("Influencer not found.");
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
                    outputDevice.displayMessage("Enter new number of followers:");
                    int newFollowers = -1;
                    while (newFollowers == -1) {
                        try {
                            newFollowers = inputDevice.readInt(); // Try reading integer
                        } catch (InvalidNumberException e) {
                            outputDevice.displayMessage(e.getMessage()); // Display error message
                        }
                    }
                    editInfluencerDetails(selectedInfluencer.getName(), newFollowers);
                    break;
                case 4:
                    viewInfluencerDetails(selectedInfluencer);
                    break;
                case 5:
                    return; // Exit the dashboard and return to the main menu
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
                    StorageUtils.saveCampaign(campaigns.keySet());

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

    private void createPost() throws CustomException.InvalidNumberException{
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

        outputDevice.displayMessage("Enter campaign name: ");
        String campaignName = inputDevice.readLine();

        // Parse and validate the date
        LocalDate postDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            postDate = LocalDate.parse(dateString, formatter);

            if (!isDateValid(postDate)) {
                outputDevice.displayMessage("The date must not be in the past. Post not created.");
                return;
            }
        } catch (DateTimeParseException e) {
            outputDevice.displayMessage("Invalid date format or invalid date. Post not created.");
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
    }
    private int parseNumber(String input, String field) throws CustomException.InvalidNumberException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new CustomException.InvalidNumberException("Invalid input for " + field + ": " + input);
        }
    }

    private void addInfluencer() throws InvalidNumberException {
            outputDevice.displayMessage("Enter the name of the influencer: ");
            String name = inputDevice.readLine();

            if (!isInfluencerNameUnique(name)) {
                outputDevice.displayMessage("An influencer with this name already exists. Influencer not added.");
                return;
            }

            outputDevice.displayMessage("Enter the platform: ");
            String platform = inputDevice.readLine();

            outputDevice.displayMessage("Enter the number of followers: ");
            int followers = inputDevice.readInt();
            inputDevice.readLine();  // Clear buffer

            String id = UUID.randomUUID().toString(); // Generate unique ID
            Influencer influencer = new Influencer(id, name, platform, followers);
            influencers.add(influencer);

            outputDevice.displayMessage("Influencer added successfully.");
        }


    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert instanceof java.sql.Date) {
            // Convert java.sql.Date to java.util.Date
            dateToConvert = new java.util.Date(dateToConvert.getTime());
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }




}