package com.sd.stackoverflow.repository;

import com.sd.stackoverflow.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface iTagRepository extends JpaRepository<Tag, Long> {
}
