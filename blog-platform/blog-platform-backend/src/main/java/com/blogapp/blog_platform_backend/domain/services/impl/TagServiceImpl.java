package com.blogapp.blog_platform_backend.domain.services.impl;

import com.blogapp.blog_platform_backend.domain.entities.Tag;
import com.blogapp.blog_platform_backend.domain.repositories.TagRepository;
import com.blogapp.blog_platform_backend.domain.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        // fetch the List of existing tags from DB
        List<Tag> existingTags = tagRepository.findByNameInIgnoreCase(tagNames);

        // extract names of each tag from List of Tag objects snd store in a Set
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        // if any name does not exist previously, create a new obj and add that to List of new tags
        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .collect(Collectors.toList());

        // save the new tags in DB
        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty()){
            tagRepository.saveAll(newTags);
        }

        // add the existing tags to the List which is to be returned to controller
        savedTags.addAll(existingTags);

        // this contains all the tags existing the system (new tags + existing tags)
        return savedTags;
    }

    @Transactional
    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if(!tag.getPosts().isEmpty()){
                throw new IllegalStateException("Cannot delete tag with posts");
            }
            tagRepository.deleteById(id);
        });
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("tag not found with id: "+id));
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if(foundTags.size() != ids.size()){
            throw new EntityNotFoundException("Not all specified tag IDs exist");
        }
        return foundTags;
    }
}
