package com.azure.samples.Dao;

import com.azure.autoconfigure.CosmosDbAutoConfiguration;
import com.azure.cosmos.CosmosException;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;

import com.azure.cosmos.util.CosmosPagedFlux;
import com.azure.samples.models.User;
import com.azure.spring.data.cosmos.core.ReactiveCosmosOperations;
import com.azure.spring.data.cosmos.repository.support.CosmosEntityInformation;
import com.azure.spring.data.cosmos.repository.support.SimpleReactiveCosmosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDao extends SimpleReactiveCosmosRepository<User, String> {

    private final CosmosDbAutoConfiguration cosmosDbAutoConfiguration;


    @Autowired
    public UserDao(ReactiveCosmosOperations reactiveCosmosOperations, CosmosDbAutoConfiguration cosmosDbAutoConfiguration) {

        super(new CosmosEntityInformation<>(User.class),reactiveCosmosOperations);
        this.cosmosDbAutoConfiguration = cosmosDbAutoConfiguration;
    }


    @Override
    public <S extends User> Mono<S> save(S s) {
        Mono.just(s).flatMap(user -> {
            cosmosDbAutoConfiguration.getContainer("user").createItem(s).block();
            return Mono.empty();
        }).subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(User user) {
        cosmosDbAutoConfiguration.getContainer("user").deleteItem(
                user.getId(), new PartitionKey(user.getLastName())
        ).block();
        return Mono.empty();
    }

    private Flux<User> executeQuery(String query, CosmosQueryRequestOptions queryOptions, int preferredPageSize) {

        queryOptions.setQueryMetricsEnabled(false);

        CosmosPagedFlux<User> cosmosPagedFlux = cosmosDbAutoConfiguration
                .getContainer("user").queryItems(
                query, queryOptions, User.class);

        try {

            cosmosPagedFlux.byPage(preferredPageSize).flatMap(fluxResponse -> {
                log.info("Got a page of query result with " +
                        fluxResponse.getResults().size() + " items(s)"
                        + " and request charge of " + fluxResponse.getRequestCharge());

                log.info("Item Ids " + fluxResponse
                        .getResults()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()));

                return Flux.empty();
            }).blockLast();

        } catch(Exception err) {
            if (err instanceof CosmosException) {
                //Client-specific errors
                CosmosException cerr = (CosmosException) err;
                cerr.printStackTrace();
                log.error(String.format("Read Item failed with %s\n", cerr));
            } else {
                //General errors
                err.printStackTrace();
            }
        }
        return Flux.empty();
    }
}
