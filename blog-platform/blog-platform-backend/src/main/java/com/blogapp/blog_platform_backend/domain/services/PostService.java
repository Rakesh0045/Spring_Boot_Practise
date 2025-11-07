package com.blogapp.blog_platform_backend.domain.services;

import com.blogapp.blog_platform_backend.domain.CreatePostRequest;
import com.blogapp.blog_platform_backend.domain.UpdatePostRequest;
import com.blogapp.blog_platform_backend.domain.entities.Post;
import com.blogapp.blog_platform_backend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    Post getPost(UUID id);
    void deletePost(UUID id);
}
