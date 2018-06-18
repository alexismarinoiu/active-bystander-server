package uk.avocado.model;

import java.sql.Timestamp;
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

  @Column(name = "creator")
  private String creator;

  @Column(name = "timestamp")
  private Timestamp timestamp;

  public Thread() {
  }

  public Thread(String threadId, Status status, String creator, Timestamp timestamp) {
    this.threadId = threadId;
    this.status = status;
    this.creator = creator;
    this.timestamp = timestamp;
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

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
