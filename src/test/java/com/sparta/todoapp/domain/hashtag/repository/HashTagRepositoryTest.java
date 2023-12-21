package com.sparta.todoapp.domain.hashtag.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class HashTagRepositoryTest {

    @Autowired
    HashTagRepository hashTagRepository;

    @Test
    @DisplayName("해시태그 저장 테스트")
    void 해시태그_저장_테스트() {
        // Given
        HashTag hashTag = HashTag.builder().name("#프랭크오션").build();
        // When
        HashTag savedHashTag = hashTagRepository.save(hashTag);
        // Then
        assertThat(savedHashTag).isNotNull();
        assertThat(savedHashTag.getName()).isEqualTo(hashTag.getName());
    }

    @Test
    @DisplayName("저장된_해시태그_이름으로_찾을 수 있다")
    void 저장된_해시태그_찾기() {
        // Given
        HashTag hashTag = HashTag.builder().name("#프랜치키위쥬스").build();
        hashTagRepository.save(hashTag);
        // When
        HashTag foundHashTag = hashTagRepository.findHashTagByName(hashTag.getName());
        // Then
        assertThat(foundHashTag).isNotNull();
        assertThat(foundHashTag.getName()).isEqualTo(hashTag.getName());
    }
}