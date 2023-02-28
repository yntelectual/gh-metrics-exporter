package io.ghmetrics;

import io.smallrye.mutiny.Uni;

import java.util.List;

public interface RepoService {
    Uni<Void> init();
    Uni<Void> put(Record rec);

    Uni<Record> get(Long id);

    Uni<List<Record>> list();
}
