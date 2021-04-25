package by.integrator.telegrambot.repositories;

import by.integrator.telegrambot.model.PostponeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostponeMessageRepository extends JpaRepository<PostponeMessage, Long> {
    List<PostponeMessage> findByChatId(String chatId);
}
