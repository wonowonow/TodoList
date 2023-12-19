package com.sparta.todoapp.domain.hashtag.repository;

import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    boolean existsHashTagByName(String name);

    HashTag findHashTagByName(String name);
}
