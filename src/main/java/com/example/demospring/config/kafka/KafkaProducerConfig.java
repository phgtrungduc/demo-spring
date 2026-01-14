package com.example.demospring.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        // ====== CÁC CONFIG QUAN TRỌNG ĐỂ TẠO BATCH ======
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);

        // LINGER_MS: Đợi tối đa X ms để gom nhiều message vào 1 batch trước khi gửi
        // Nếu batch đầy (đạt BATCH_SIZE) hoặc hết thời gian chờ → gửi ngay
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 10000);  // 10s - test batching
        
        // BATCH_SIZE: Kích thước MAXIMUM của 1 batch (không phải minimum!)
        // Khi batch đạt size này → gửi ngay (không đợi LINGER_MS)
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16 * 1024);   // 16KB (đủ cho ~80-100 messages)

        // ====== TIMEOUT ======
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30_000);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120_000);

        // ====== BUFFER ======
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33_554_432);



        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
