package by.integrator.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "answered", columnDefinition = "TINYINT(0)")
    private Boolean isAnswered;

    @Column(name = "isSent", columnDefinition = "TINYINT(0)")
    private Boolean isSent;

    @Column(name = "question", columnDefinition = "LONGTEXT")
    private String question;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ClientId")
    private Client client;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "currentQuestion", cascade = CascadeType.PERSIST)
    private Set<Admin> currentAdmins;
}
