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

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "profileFilled", columnDefinition = "TINYINT(0)")
    private Boolean profileFilled;

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

    public String getTelegramId() {
        return user.getTelegramId();
    }

}
