package by.integrator.telegrambot.repositories;

import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByClient(Client client);
    Integer countAllByClientAndIsAnswered(Client client, Boolean isAnswered);
    Question findByClientAndIsAnswered(Client client, Boolean isAnswered);
    Question findByClientAndIsSent(Client client, Boolean isSent);
    List<Question> findAllByIsSentAndIsAnswered(Boolean isSent, Boolean isAnswered);
}
