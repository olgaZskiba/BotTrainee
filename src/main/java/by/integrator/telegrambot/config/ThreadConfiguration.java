package by.integrator.telegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ThreadConfiguration {
    
    private final static Integer AWAIT_TERMINATON_SECONDS = 3;

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(Integer.MAX_VALUE);
        threadPoolTaskExecutor.setMaxPoolSize(Integer.MAX_VALUE);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(AWAIT_TERMINATON_SECONDS);
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

}
