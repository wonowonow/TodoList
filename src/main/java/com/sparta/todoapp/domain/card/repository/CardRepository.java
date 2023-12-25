package com.sparta.todoapp.domain.card.repository;

import com.sparta.todoapp.domain.card.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardQueryRepository {

    List<Card> findAllByOrderByCreatedAtDesc();
}
