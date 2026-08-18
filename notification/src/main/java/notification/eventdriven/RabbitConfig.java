package notification.eventdriven;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TASK_EXCHANGE = "task.events.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.events.queue";
    public static final String KEY = "task.events.key";

    @Bean
    public TopicExchange taskExchange() {
        return new TopicExchange(TASK_EXCHANGE, false, false);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, false);
    }

    @Bean
    public Binding analyticsBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(taskExchange())
                .with("task.#");
    }

    @Bean
    public MessageConverter messageConverter(
            ObjectMapper objectMapper
    ) {
        return new Jackson2JsonMessageConverter(
                objectMapper
        );
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(
                messageConverter
        );

        return rabbitTemplate;
    }

}