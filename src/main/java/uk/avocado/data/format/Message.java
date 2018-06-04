package uk.avocado.data.format;

import java.sql.Timestamp;

public class Message {

  private String sender;
  private int seq;
  private Timestamp timestamp;
  private String content;
  private String threadid;

  public Message(String sender, int seq, Timestamp timestamp, String content, String threadid) {
    this.sender = sender;
    this.seq = seq;
    this.timestamp = timestamp;
    this.content = content;
    this.threadid = threadid;
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

  public String getThreadid() {
    return threadid;
  }
}
