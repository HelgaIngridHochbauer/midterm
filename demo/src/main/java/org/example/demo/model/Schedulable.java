package org.example.demo.model;

import org.example.demo.model.Post;

import java.util.List;

public interface Schedulable {
    void schedulePost(Post post);
    List<Post> getPostsByMonth(int month, int year);
}