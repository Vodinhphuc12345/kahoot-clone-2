package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.presentation.Chat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findAllByPresentationIdAndIdLessThan(int presentationId, int chatId, Pageable pageRequest);
    List<Chat> findAllByPresentationId(int presentationId, Pageable pageRequest);
}
