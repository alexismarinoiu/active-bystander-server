package uk.avocado.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "thread")
public class Thread {
  @Id
  @Column(name = "threadId")
  private String threadId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @OneToMany
  @JoinColumn(name = "threadId")
  private List<Participant> participants;


  public String getThreadId() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

  public List<Participant> getParticipants() {
    return participants;
  }
}
