package uk.avocado.database;

public interface DatabaseSession {

  void close();

  DatabaseTransaction beginTransaction();

  void saveOrUpdate(Object object);

  <R> DatabaseQuery<R> createQuery(String query, Class<R> targetClass);

}