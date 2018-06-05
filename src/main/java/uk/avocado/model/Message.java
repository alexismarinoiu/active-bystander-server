package uk.avocado.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "message")
public class Message {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
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

  @OneToMany(mappedBy = "threadId")
  private List<Participant> participants;

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

  public List<Participant> getParticipants() {
    return participants;
  }
}
