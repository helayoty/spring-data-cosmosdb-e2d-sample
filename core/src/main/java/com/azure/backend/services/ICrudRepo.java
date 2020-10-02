package com.azure.backend.services;

import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface ICrudRepo<BaseDTO, String> extends ReactiveCosmosRepository<BaseDTO, String> {

    /**
     * Common template method used to execute and retrieve queries for the given type using the passed
     * in query.
     */
    <T> Flux<T> getAll(Class<T> classType, SqlQuerySpec sqlQuerySpec);

    Mono<Void> queryDocuments();

    /**
     * Read the document only if it has been changed, utilizing an ETag check.
     * @return
     */
    Mono<Void> readDocumentOnlyIfChanged();

    /**
     * Replace the document employing optimistic concurrency using ETag.
     * @return
     */
    Mono<Void> replaceDocumentWithConditionalEtagCheck();

}
