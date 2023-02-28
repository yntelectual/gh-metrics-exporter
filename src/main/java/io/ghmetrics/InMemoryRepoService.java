package io.ghmetrics;

import io.quarkus.arc.lookup.LookupIfProperty;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@LookupIfProperty(name = "repo.type", stringValue = "memory")

@ApplicationScoped
@Slf4j
public class InMemoryRepoService implements RepoService {
    private Map<Long, Record> store = new ConcurrentHashMap<>();

    @Override
    public Uni<Void> init() {
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> put(Record rec) {
        this.store.put(rec.getId(), rec);
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Record> get(Long id) {
        return Uni.createFrom().item(store.get(id));
    }

    @Override
    public Uni<List<Record>> list() {
        return Uni.createFrom().item(new ArrayList<>(store.values()));
    }
}
