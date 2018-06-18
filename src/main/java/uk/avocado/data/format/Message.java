package uk.avocado.data.format;

import java.sql.Timestamp;

public class Message {

  private String sender;
  private int seq;
  private Timestamp timestamp;
  private String content;
  private String threadId;

  public Message() {

  }

  public Message(uk.avocado.model.Message message) {
    this.sender = message.getSender();
    this.seq = message.getSeq();
    this.timestamp = message.getTimestamp();
    this.content = message.getContent();
    this.threadId = message.getThreadId();
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
