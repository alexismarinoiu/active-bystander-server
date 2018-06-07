package uk.avocado.data.format;

import java.sql.Timestamp;

public class SentMessage {

  private int seq;
  private String content;

  public SentMessage() {
  }

  public int getSeq() {
    return seq;
  }

  public String getContent() {
    return content;
  }

  public void setSeq(int seq) {
    this.seq = seq;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
