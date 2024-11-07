package com.hunghq.librarymanagement.IGeneric;

import java.sql.ResultSet;
import java.util.List;

public interface IRepository<T>
{
    T make(ResultSet reS);
    void add(T entity);
    T getById(String id);
    List<T> getAll();
    List<T> findByName(String name);
    T save(T entity);
    void update(T entity);
    void delete(String id);
}
