package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import uk.avocado.database.PostgreSQLEnumType;

@Entity
@Table(name = "thread")
@TypeDef(name = "psqlEnum", typeClass = PostgreSQLEnumType.class)
public class Thread {

  @Id
  @Column(name = "threadId")
  private String threadId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  @Type(type = "psqlEnum")
  private Status status;

  public Thread() {
  }

  public Thread(String threadId, Status status) {
    this.threadId = threadId;
    this.status = status;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getThreadId() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

}
