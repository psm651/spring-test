package com.study.spring.module.event.repository;

import com.study.spring.module.event.domain.Alarm;
import com.study.spring.module.event.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
}
