package uk.avocado.database;

import org.hibernate.Transaction;

public class HibernateTransactionAdapter implements DatabaseTransaction {

  private final Transaction transaction;

  public HibernateTransactionAdapter(Transaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public void commit() {
    transaction.commit();
  }
}
