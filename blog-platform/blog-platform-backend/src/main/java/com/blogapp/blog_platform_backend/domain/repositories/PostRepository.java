package com.blogapp.blog_platform_backend.domain.repositories;

import com.blogapp.blog_platform_backend.domain.PostStatus;
import com.blogapp.blog_platform_backend.domain.entities.Category;
import com.blogapp.blog_platform_backend.domain.entities.Post;
import com.blogapp.blog_platform_backend.domain.entities.Tag;
import com.blogapp.blog_platform_backend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus postStatus, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus postStatus, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus postStatus, Tag tag);
    List<Post> findAllByStatus(PostStatus postStatus);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus postStatus);
}
