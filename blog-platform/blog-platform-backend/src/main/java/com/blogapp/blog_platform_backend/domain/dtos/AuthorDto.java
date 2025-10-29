package com.blogapp.blog_platform_backend.domain.dtos;

import com.blogapp.blog_platform_backend.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    private UUID id;
    private String name;
}
