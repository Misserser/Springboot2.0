package com.example.demo.controller;

import com.example.demo.entity.Chapter;
import com.example.demo.entity.Image;
import com.example.demo.repository.ChapterRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.function.Supplier;

@RestController
public class ChapterController
{
    private final ChapterRepository repository;

    public ChapterController(ChapterRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/chapters")
    public Flux<Chapter> listing()
    {
        return repository.findAll();
    }

    @GetMapping("/fluxTest")
    public Flux<String> getFluxList()
    {
       Flux.just((Supplier<String>) () -> "alpha",
                (Supplier<String>) () -> "bravo",
                (Supplier<String>) () -> "cat")
                .subscribe(s -> System.out.println(s.get()));

        return Flux.just("alpha", "bravo", "cat")
                .map(String::toUpperCase)
                .flatMap(s -> Flux.fromArray(s.split("")))
                .groupBy(String::toString)
                .sort((o1, o2) -> o1.key().compareTo(o2.key()))
                //.flatMap(group -> Mono.just(group.key()).and(group.count()))
                .flatMap(group -> Mono.zip(Mono.just(group.key()), group.count()))
                .map(keyAndCount -> keyAndCount.getT1() + " => " + keyAndCount.getT2() + "\n");
                //.subscribe(System.out::println);
    }
}
