package com.example.demo.repository;

import com.example.demo.entity.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface
ChapterRepository extends ReactiveCrudRepository<Chapter, String>
{

}
