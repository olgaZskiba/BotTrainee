package by.integrator.telegrambot.service;

import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Question;
import by.integrator.telegrambot.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public void save(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    @Transactional
    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    @Transactional
    public List<Question> getAllByClient(Client client) {
        return questionRepository.findAllByClient(client);
    }

    @Transactional
    public Integer getAllByClientAndIsAnswered(Client client, Boolean isAnswered) {
        return questionRepository.countAllByClientAndIsAnswered(client, isAnswered);
    }

    public void createQuestion(Client client, String text) {
        Question question = Question.builder()
                .question(text)
                .isAnswered(false)
                .isSent(false)
                .client(client)
                .build();

        save(question);
    }

    @Transactional
    public void saveQuestionText(Client client, String text) {
        Question question = questionRepository.findByClientAndIsAnswered(client, false);
        if (question != null) {
            String questionText = question.getQuestion();
            questionText = questionText.concat("\n");
            questionText = questionText.concat(text);
            question.setQuestion(questionText);

            save(question);
        }
    }

    @Transactional
    public Question getByClientAndIsSent(Client client, Boolean isSent) {
        return questionRepository.findByClientAndIsSent(client, isSent);
    }

    @Transactional
    public List<Question> getAllByIsSentAndIsAnswered(Boolean isSent, Boolean isAnswered) {
        return questionRepository.findAllByIsSentAndIsAnswered(isSent, isAnswered);
    }

    public Question getById(int questionId) {
        return questionRepository.findById((long)questionId).orElse(null);
    }
}
