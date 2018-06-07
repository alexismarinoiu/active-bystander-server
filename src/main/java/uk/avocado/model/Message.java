package uk.avocado.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "sender")
  private String sender;

  @Column(name = "seq")
  private int seq;

  @Column(name = "timestamp")
  private Timestamp timestamp;

  @Column(name = "content")
  private String content;

  @Column(name = "threadId")
  private String threadId;

  public Message() {
  }

  public Message(String sender, int seq, Timestamp timestamp, String content, String threadId) {
    this.sender = sender;
    this.seq = seq;
    this.timestamp = timestamp;
    this.content = content;
    this.threadId = threadId;
  }

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

  public String getThreadId() {
    return threadId;
  }

}
