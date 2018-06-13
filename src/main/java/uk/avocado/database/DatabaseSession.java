package uk.avocado.database;

import java.io.Serializable;

public interface DatabaseSession {

  void close();

  DatabaseTransaction beginTransaction();

  void saveOrUpdate(Object object);

  void delete(Object object);

  <R> DatabaseQuery<R> createQuery(String query, Class<R> targetClass);

  Serializable save(Object object);
}
