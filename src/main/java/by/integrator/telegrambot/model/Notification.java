package by.integrator.telegrambot.model;

import by.integrator.telegrambot.model.enums.NotificationType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "text", nullable = false, columnDefinition = "LONGTEXT")
    private String text;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private NotificationType type;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "currentNotification", cascade = CascadeType.PERSIST)
    private Set<Admin> currentAdmins;
}
