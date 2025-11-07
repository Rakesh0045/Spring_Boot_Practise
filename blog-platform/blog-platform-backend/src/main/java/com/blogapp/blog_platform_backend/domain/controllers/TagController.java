package com.blogapp.blog_platform_backend.domain.controllers;

import com.blogapp.blog_platform_backend.domain.dtos.CreateTagsRequest;
import com.blogapp.blog_platform_backend.domain.dtos.TagDto;
import com.blogapp.blog_platform_backend.domain.entities.Tag;
import com.blogapp.blog_platform_backend.domain.mappers.TagMapper;
import com.blogapp.blog_platform_backend.domain.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags(){
        List<Tag> tags = tagService.getTags();
        List<TagDto> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagsRequest createTagsRequest){
        List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());
        List<TagDto> createdTagResponses = savedTags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();
        return new ResponseEntity<>(createdTagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}

