package by.integrator.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "postpone_message")
public class PostponeMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "text", columnDefinition = "LONGTEXT")
    private String text;

    @Column(name = "pictureUrl", columnDefinition = "LONGTEXT")
    private String pictureUrl;

    @Column(name = "date")
    private Date date;

    @Column(name = "chatId", nullable = false)
    private String chatId;

    @Column(name = "IsLast", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isLast;
}
