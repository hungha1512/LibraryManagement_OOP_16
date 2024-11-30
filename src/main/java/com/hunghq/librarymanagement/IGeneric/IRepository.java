package com.hunghq.librarymanagement.IGeneric;

import javafx.collections.ObservableList;

import java.sql.ResultSet;

public interface IRepository<T>
{
    T make(ResultSet reS);
    void add(T entity);
    T getByStringId(String id);
    T getByIntId(int id);
    ObservableList<T> getAll();
    ObservableList<T> findByName(String name);
    T save(T entity);
    boolean update(T entity);
    void delete(String id);
}
