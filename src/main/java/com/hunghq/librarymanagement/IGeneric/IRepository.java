package com.hunghq.librarymanagement.IGeneric;

import java.sql.ResultSet;
import java.util.List;

public interface IRepository<T>
{
    T Make(ResultSet reS);
    void Add(T entity);
    T GetById(String id);
    List<T> GetAll();
    List<T> FindByName(String name);
    T Save(T entity);
    void Update(T entity);
    void Delete(int id);
    
}
