package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "events")
public class Event {
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests; //кол-во одобренных заявок
    @Column(name = "created_on")
    private LocalDateTime createdOn; //Creation time
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private Boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "title")
    private String title;
    @Transient
    //TODO: либо все таки засунуть в бд
    private Long views;
}
