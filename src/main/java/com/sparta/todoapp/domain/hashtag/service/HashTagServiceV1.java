package com.sparta.todoapp.domain.hashtag.service;

import static com.sparta.todoapp.domain.hashtag.entity.HashTag.*;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import com.sparta.todoapp.domain.card_hashtag.repository.CardHashTagRepository;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import com.sparta.todoapp.domain.hashtag.repository.HashTagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagServiceV1 implements HashTagService {

    private final HashTagRepository hashTagRepository;

    private final CardHashTagRepository cardHashTagRepository;

    @Override
    public List<String> findHashTagByCardContent(Card card) {

        Pattern hasgTagPattern = Pattern.compile("#[^\\s#]+");
        Matcher matcher = hasgTagPattern.matcher(card.getContent());

        List<String> hashTagList = new ArrayList<>();

        while (matcher.find()) {
            hashTagList.add(matcher.group(0));
        }
        // 분리 하는 게 낫겠지?
//        saveTag(hashTagList, card);
        return hashTagList;
    }

    @Override
    public void saveTag(List<String> hashTagList, Card card) {

        for (String name : hashTagList) {

            // 저장 안 된 태그는 먼저 태그 테이블에 저장
            if (!hashTagRepository.existsHashTagByName(name)) {
                HashTag hashTag = builder().name(name).build();
                hashTagRepository.save(hashTag);
            }

            // 이미 저장 된 태그라면 밑으로 빠지고
            // 조인 테이블에 카드 및 태그 저장

            HashTag hashTag = hashTagRepository.findHashTagByName(name);
            CardHashTag cardHashTag = new CardHashTag(card, hashTag);
            cardHashTagRepository.save(cardHashTag);
        }
    }
}
