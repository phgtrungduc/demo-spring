package com.example.demospring.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
public class KafkaGlobalErrorHandler implements CommonErrorHandler {
    @Override
    public boolean handleOne(
            Exception thrownException,
            ConsumerRecord<?, ?> record,
            Consumer<?, ?> consumer,
            MessageListenerContainer container) {
        log.error("Global error handler for message: {}, {}", record.value().toString(), thrownException.getMessage());
        return false;
    }

    @Override
    public void handleOtherException(
            Exception thrownException,
            Consumer<?, ?> consumer,
            MessageListenerContainer container,
            boolean batchListener) {
        log.error("Global error handler for message: {}", thrownException.getMessage());
    }
}
