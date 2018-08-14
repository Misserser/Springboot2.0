package com.example.demo.service;

import com.example.demo.entity.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService
{
    private static String UPLOAD_ROOT = "upload_dir";

    private final ResourceLoader resourceLoader;

    public  ImageService(ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    public Flux<Image> findAllImage()
    {
        try
        {
            return Flux.fromIterable(
                    Files.newDirectoryStream(Paths.get(UPLOAD_ROOT)))
                    .map(path -> new Image(path.hashCode()+"", path.getFileName().toString()));
                    //.flatMap(path -> new Image(path.hashCode()+"", path.getFileName().toString())).then();

            //public final <R> Flux<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
            //public final <V> Flux<V> map(Function<? super T, ? extends V> mapper) {
        }
        catch (IOException e)
        {
            return Flux.empty();
        }
    }

    public Mono<Resource> findOneImage(String filename)
    {
        return Mono.fromSupplier(() ->
        resourceLoader.getResource("file:"+UPLOAD_ROOT+"/"+filename));
    }

    public Mono<Void> createImage(Flux<FilePart> files)
    {
        return files.flatMap(file -> file.transferTo(Paths.get(UPLOAD_ROOT, file.filename()).toFile())).then();
    }

    public Mono<Void> deleteImage(String filename)
    {
        return Mono.fromRunnable(() ->
        {
            try
            {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
    }

//    @Bean
//    CommandLineRunner setUp() throws IOException
//    {
//        return (args) ->
//        {
//            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
//
//            Files.createDirectory(Paths.get(UPLOAD_ROOT));
//
//            FileCopyUtils.copy("Test File", new FileWriter(UPLOAD_ROOT + "/learning-spring-boot-cover.jpg"));
//            FileCopyUtils.copy("Test File2", new FileWriter(UPLOAD_ROOT + "/learning-spring-2nd-edition-cover.jpg"));
//            FileCopyUtils.copy("Test File3", new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
//        };
//    }
}
