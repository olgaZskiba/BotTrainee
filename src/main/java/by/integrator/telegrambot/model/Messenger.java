package by.integrator.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "messenger")
@AllArgsConstructor
@NoArgsConstructor
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "client_messeger",
            joinColumns = @JoinColumn(name = "MessegerId"),
            inverseJoinColumns = @JoinColumn(name = "ClientId"))
    private Set<Client> clients = new HashSet<>();
}
