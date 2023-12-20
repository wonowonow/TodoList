package com.sparta.todoapp.domain.card.repository;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.user.entity.User;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardQueryRepository {

    List<Card> findAllByOrderByCreatedAtDesc();
}
