package uk.avocado.database;

import java.util.List;

public interface DatabaseQuery<R> {

  List<R> list();

  DatabaseQuery<R> setParameter(String param, Object value);

  DatabaseQuery<R> setMaxResults(int maxResults);

  void executeUpdate();
}
