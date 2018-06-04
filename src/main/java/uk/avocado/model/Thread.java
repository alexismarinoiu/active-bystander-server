package uk.avocado.model;

import uk.avocado.data.format.Participant;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "thread")
public class Thread {

  @Id
  @Column(name = "threadid")
  private String threadid;

  @Column(name = "status")
  private Status status;

  @OneToMany
  @JoinColumn(name = "threadid")
  private List<Participant> participants;


  public String getThreadid() {
    return threadid;
  }

  public Status getStatus() {
    return status;
  }

  public List<Participant> getParticipants() {
    return participants;
  }
}
