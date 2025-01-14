package org.example.demo.model;

import org.example.demo.model.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PostScheduler implements Schedulable {
    private List<Post> scheduledPosts;

    public PostScheduler() {
        this.scheduledPosts = new ArrayList<>();
    }

    @Override
    public void schedulePost(Post post) {
        scheduledPosts.add(post);
    }

    @Override
    public List<Post> getPostsByMonth(int month, int year) {
        List<Post> postsInMonth = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (Post post : scheduledPosts) {
            calendar.setTime(post.getDate());
            int postMonth = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is zero-based
            int postYear = calendar.get(Calendar.YEAR);

            if (postMonth == month && postYear == year) {
                postsInMonth.add(post);
            }
        }

        return postsInMonth;
    }
}