package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {

  @Column(name = "sender")
  private String sender;

  @Column(name = "seq")
  private int seq;

  @Column(name = "timestamp")
  private Timestamp timestamp;

  @Column(name = "content")
  private String content;

  @Column(name = "threadid")
  private String threadid;

  public String getSender() {
    return sender;
  }

  public int getSeq() {
    return seq;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public String getContent() {
    return content;
  }

  public String getThreadid() {
    return threadid;
  }
}
