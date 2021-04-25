package by.integrator.telegrambot.service;

import by.integrator.telegrambot.model.PostponeMessage;
import by.integrator.telegrambot.repositories.PostponeMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostponeMessageService {
    @Autowired
    private PostponeMessageRepository postponeMessageRepository;

    @Transactional
    public void save(PostponeMessage postponeMessage) {
        postponeMessageRepository.save(postponeMessage);
    }

    @Transactional
    public void delete(PostponeMessage postponeMessage) {
        postponeMessageRepository.delete(postponeMessage);
    }

    @Transactional
    public List<PostponeMessage> getAll() {
        return postponeMessageRepository.findAll().stream().filter(m -> !m.getIsLast()).collect(Collectors.toList());
    }

    @Transactional
    public List<PostponeMessage> getAllWithoutLast() {
        System.out.println(getAll());
        return getAll().stream().filter(m -> !m.getIsLast()).collect(Collectors.toList());
    }

    @Transactional
    public PostponeMessage getLastByChatId(String chatId) {
        return postponeMessageRepository.findByChatId(chatId).stream().filter(PostponeMessage::getIsLast).findAny().orElse(null);
    }

    @Transactional
    public PostponeMessage getByDateHashcode(int hashCode) {
        List<PostponeMessage> postponeMessageList = postponeMessageRepository.findAll();
        for (PostponeMessage postponeMessage : postponeMessageList) {
            if (postponeMessage.getDate().hashCode() == hashCode) {
                return postponeMessage;
            }
        }
        return null;
    }

    @Transactional
    public PostponeMessage getById(int parseInt) {
        return postponeMessageRepository.findById((long)parseInt).orElse(null);
    }
}
