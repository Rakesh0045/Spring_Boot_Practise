package com.blogapp.blog_platform_backend.domain.services.impl;

import com.blogapp.blog_platform_backend.domain.CreatePostRequest;
import com.blogapp.blog_platform_backend.domain.PostStatus;
import com.blogapp.blog_platform_backend.domain.UpdatePostRequest;
import com.blogapp.blog_platform_backend.domain.entities.Category;
import com.blogapp.blog_platform_backend.domain.entities.Post;
import com.blogapp.blog_platform_backend.domain.entities.Tag;
import com.blogapp.blog_platform_backend.domain.entities.User;
import com.blogapp.blog_platform_backend.domain.repositories.PostRepository;
import com.blogapp.blog_platform_backend.domain.services.CategoryService;
import com.blogapp.blog_platform_backend.domain.services.PostService;
import com.blogapp.blog_platform_backend.domain.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(createPostRequest.getTagIds());
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Transactional
    @Override
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {

        //retrieve existing post from DB
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Post does not exist with id: "+id));

        //set values to existingPost from updated post request
        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent();
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        //check if category has been changed, if changed then change the category of existing post to new one
        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        //check if tags has been changed, if changed then change the tags of existing post to new one
        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();

        if(!existingTagIds.equals(updatePostRequestTagIds)){
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);

    }

    @Override
    public Post getPost(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist with id: "+id));
        return post;
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;

        return (int)Math.ceil((double) wordCount / WORDS_PER_MINUTE);

    }
}
