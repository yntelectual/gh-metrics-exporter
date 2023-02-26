package io.ghmetrics;

import io.micrometer.core.annotation.Timed;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import javax.inject.Singleton;
import java.util.List;

@Startup
@Singleton
@Slf4j
public class DynamoRepoService {
    private DynamoDbAsyncTable<Record> recordTable;
    private DynamoDbEnhancedAsyncClient enhancedAsyncClient;

    DynamoRepoService(DynamoDbAsyncClient asyncClient) {
        this.enhancedAsyncClient = DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(asyncClient)
                .build();
        recordTable = enhancedAsyncClient.table("GithubRuns",
                TableSchema.fromClass(Record.class));

        recordTable.describeTable().whenComplete((describeTableEnhancedResponse, throwable) -> {
            log.info("Dynamo table exists alread {}", describeTableEnhancedResponse);
        }).exceptionally(throwable -> {
            if (throwable.getMessage().contains("ResourceNotFoundException")) {
                log.info("Table GithubRuns does not exist yet, creating now...");
                recordTable.createTable().whenComplete((unused, throwable2) -> {
                    recordTable.describeTable().whenComplete((describeTableEnhancedResponse, throwable1) -> {
                        log.info("&& Table {} e {}", describeTableEnhancedResponse, throwable1);
                    });
                }).thenAccept(unused -> {
                    log.info("GF run table created");
                });
            } else {
                throw new IllegalStateException("Cant describe dynamo DB table {}", throwable);
            }
            return null;
        });
    }

    @Timed("dynamo.put")
    public Uni<Void> put(Record rec) {
        return Uni.createFrom().future(recordTable.putItem(rec));
    }

    @Timed("dynamo.get")
    public Uni<Record> get(Long id) {
        return Uni.createFrom().future(recordTable.getItem(r -> r.key(Key.builder().partitionValue(id).build())));
    }

    public Uni<List<Record>> list() {
        return Uni.createFrom().publisher(recordTable.scan()).onItem().transform(recordPage -> recordPage.items());
    }

}