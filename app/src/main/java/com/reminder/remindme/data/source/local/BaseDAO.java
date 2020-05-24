package com.reminder.remindme.data.source.local;

import java.util.List;


public interface BaseDAO<A> {
    int count();
    List<A> select();
    Long insert(A item);
    List<Long> insert(A... items);
    Long update(A item);
    List<Long> update(A... items);
    Long delete(A item);
    void deleteAll();
}
