package se331.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;
    String category, title, description, location, date, time;
    Boolean petAllowed;
    @ManyToOne
    Organizer organizer;
    @Builder.Default
    @ManyToMany(mappedBy = "eventHistory")
    List<Participant> participants = new ArrayList<>();
    @ElementCollection
    List<String> images;
}
