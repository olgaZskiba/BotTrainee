package by.integrator.telegrambot.model;

import javax.persistence.*;

import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import by.integrator.telegrambot.model.enums.BotType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Table(name = "client")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_bot_state", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ClientBotState clientBotState;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "problem", columnDefinition = "LONGTEXT")
    private String problem;

    @Column(name = "goals")
    private String goals;

    @Column(name = "fieldOfActivity")
    private String fieldOfActivity;

    @Column(name = "integrationToCrm")
    private String integrationToCrm;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "userMessenger")
    private String userMessenger;

    @Column(name = "wayCommunication")
    private String wayCommunication;

    @Column(name = "day")
    private Integer day;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "currentClient", cascade = CascadeType.PERSIST)
    private Set<Admin> currentAdmins;

    @Column(name = "profileFilled", columnDefinition = "TINYINT(0)")
    private Boolean profileFilled;

    @Column(name = "processed", columnDefinition = "TINYINT(0)")
    private Boolean processed;

    @Column(name = "fillStarted", columnDefinition = "TINYINT(0)")
    private Boolean fillStarted;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private User user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "client_messeger",
            joinColumns = @JoinColumn(name = "ClientId"),
            inverseJoinColumns = @JoinColumn(name = "MessegerId"))
    private Set<Messenger> messengers = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.ALL })
    private Set<Question> questions;

    public String getTelegramId() {
        return user.getTelegramId();
    }

}
