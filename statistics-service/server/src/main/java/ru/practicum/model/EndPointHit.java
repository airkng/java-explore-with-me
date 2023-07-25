package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "views_stats")
//Будет сохраняться в бд
public class EndPointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Integer id;

    @Column(name = "app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "created")
    private LocalDateTime timestamp;
}
