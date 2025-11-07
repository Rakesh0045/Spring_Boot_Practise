package com.blogapp.blog_platform_backend.domain.mappers;

import com.blogapp.blog_platform_backend.domain.CreatePostRequest;
import com.blogapp.blog_platform_backend.domain.UpdatePostRequest;
import com.blogapp.blog_platform_backend.domain.dtos.CreatePostRequestDto;
import com.blogapp.blog_platform_backend.domain.dtos.PostDto;
import com.blogapp.blog_platform_backend.domain.dtos.UpdatePostRequestDto;
import com.blogapp.blog_platform_backend.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
