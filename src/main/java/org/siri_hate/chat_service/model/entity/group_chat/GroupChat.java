package org.siri_hate.chat_service.model.entity.group_chat;

import jakarta.persistence.*;
import org.siri_hate.chat_service.model.entity.Chat;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("GROUP")
public class GroupChat extends Chat {

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GroupChatParticipant> groupParticipants = new HashSet<>();

  public GroupChat() {
  }

  public GroupChat(String name) {
    this.name = name;
  }

  public GroupChat(String name, Set<GroupChatParticipant> groupParticipants) {
    this.name = name;
    this.groupParticipants = groupParticipants;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<GroupChatParticipant> getGroupParticipants() {
    return groupParticipants;
  }

  public void setGroupParticipants(Set<GroupChatParticipant> groupParticipants) {
    this.groupParticipants = groupParticipants;
  }

  public void addParticipant(GroupChatParticipant participant) {
    groupParticipants.add(participant);
    participant.setChat(this);
  }

  public void removeParticipant(GroupChatParticipant participant) {
    groupParticipants.remove(participant);
    participant.setChat(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GroupChat)) return false;
    if (!super.equals(o)) return false;
    return getId() != null && getId().equals(((GroupChat) o).getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
