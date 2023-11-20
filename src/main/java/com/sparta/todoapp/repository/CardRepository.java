package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByUser(User user);

    List<Card> findAllByOrderByCreatedAtDesc();

}
