package uk.avocado.database;

import java.util.List;
import org.hibernate.query.Query;

public class HibernateQueryAdapter<R> implements DatabaseQuery<R> {

  private final Query<R> query;

  public HibernateQueryAdapter(Query<R> query) {
    this.query = query;
  }

  @Override
  public List<R> list() {
    return query.list();
  }

  @Override
  public DatabaseQuery<R> setParameter(String param, String value) {
    return new HibernateQueryAdapter<>(query.setParameter(param, value));
  }
}
