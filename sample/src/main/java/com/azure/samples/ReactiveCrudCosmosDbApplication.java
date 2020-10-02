package com.azure.samples;

import com.azure.samples.models.User;
import com.azure.samples.repository.ReactiveUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class  ReactiveCrudCosmosDbApplication implements CommandLineRunner {

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;

    @Autowired
    public ReactiveCrudCosmosDbApplication(ReactiveUserRepository reactiveUserRepository) {
        this.reactiveUserRepository = reactiveUserRepository;

    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveCrudCosmosDbApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        User testUser1 = new User("testId1", "testFirstName", "testLastName1");
        User testUser2 = new User("testId2", "testFirstName", "testLastName2");

        Flux<User> users = Flux.just(testUser1,testUser2);

        reactiveUserRepository.save(testUser1);
        reactiveUserRepository.save(testUser2);


        reactiveUserRepository.findByFirstName(testUser1.getFirstName());
        log.info(reactiveUserRepository.findByFirstName(testUser1.getFirstName()).toString());

       //clean-up
        reactiveUserRepository.delete(testUser1);
        reactiveUserRepository.delete(testUser2);

    }

}
