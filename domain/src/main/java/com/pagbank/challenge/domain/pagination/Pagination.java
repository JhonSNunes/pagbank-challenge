package com.pagbank.challenge.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int currentPage,
        int itemsPerPage,
        long total,
        List<T> items
) {
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> list = this.items.stream().map(mapper).toList();

        return new Pagination<>(currentPage(), itemsPerPage(), total(), list);
    }
}
