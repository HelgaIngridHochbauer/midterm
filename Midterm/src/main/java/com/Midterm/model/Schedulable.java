package com.Midterm.model;

import com.Midterm.model.Post;

import java.util.List;

public interface Schedulable {
    void schedulePost(Post post);
    List<Post> getPostsByMonth(int month, int year);
}