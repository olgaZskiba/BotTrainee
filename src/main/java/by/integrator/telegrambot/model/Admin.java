package by.integrator.telegrambot.model;

import by.integrator.telegrambot.bot.api.admin.states.AdminBotState;
import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Table(name = "admin")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "CurrentClientId")
    private Client currentClient;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "CurrentQuestionId")
    private Question currentQuestion;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "CurrentNotificationId")
    private Notification currentNotification;

    @Column(name = "admin_bot_state", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AdminBotState adminBotState;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    private User user;

    public String getTelegramId() {
        return user.getTelegramId();
    }

}
