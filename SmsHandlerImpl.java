package vn.com.vpbanks.service.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import vn.com.vpbanks.constants.SmsTemplate;
import vn.com.vpbanks.dto.sms.FlexSmsMessage;
import vn.com.vpbanks.dto.sms.KafkaNotificationMessage;
import vn.com.vpbanks.repository.EmailDAO;
import vn.com.vpbanks.repository.SmsDAO;
import vn.com.vpbanks.service.kafka.SmsHandler;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static vn.com.vpbanks.constants.FlexTopics.*;
import static vn.com.vpbanks.constants.enums.SendNoticationFlexType.SMS;

@Component
@Slf4j
@RequiredArgsConstructor
public class SmsHandlerImpl implements SmsHandler {
    private final ObjectMapper objectMapper;
    private final SmsDAO smsDAO;
    private final EmailDAO emailDAO;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = FLEX_SMS_TOPIC)
    @Transactional(rollbackFor = JsonProcessingException.class)
    public void receiveSmsMessage(@Header(KafkaHeaders.OFFSET) Integer offset, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition, @Payload String message) {
        log.info("Received message from  partition = {} ,offset = {}, message {}  ", partition, offset, message);

        FlexSmsMessage flexSmsMessage;
        try {
            flexSmsMessage = objectMapper.readValue(message, FlexSmsMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Have error during convert json to object {}", e.getMessage());
            throw new ApiException(e);
        }

        this.convertAndSendMessage(flexSmsMessage);
    }

    private void convertAndSendMessage(FlexSmsMessage flexSmsMessage) {
        Optional<SmsTemplate> smsTemplate = SmsTemplate.getTemplate(flexSmsMessage.getTemplateId());
        if (smsTemplate.isEmpty()) {
            log.info("Unknown templateId {} ", flexSmsMessage.getTemplateId());
            return;
        }

        Object message = smsDAO.retrieveData(flexSmsMessage.getQuery(), smsTemplate.get().getClazz());

        KafkaNotificationMessage kafkaNotificationMessage = KafkaNotificationMessage.builder().autoId(PREFIX_SMSEMAIL_TOPIC + flexSmsMessage.getAutoId() + "_" + UUID.randomUUID()).emailOrPhone(flexSmsMessage.getPhoneNumber()).type(SMS).templateId(flexSmsMessage.getTemplateId()).data(message).build();

        log.info("[Flex Connector] send sms message:  {} to base service ", kafkaNotificationMessage);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(NOTIFICATION_SMS_TOPIC, kafkaNotificationMessage);
        future.completable().whenComplete((success, failed) -> {
            if (failed != null) {
                emailDAO.updateEmailLog(new BigDecimal(flexSmsMessage.getAutoId()), "E", failed.getMessage());
            } else {
                emailDAO.updateEmailLog(new BigDecimal(flexSmsMessage.getAutoId()), "S", "Flex Connector sent message successfully");
                log.debug("Updated email log");
            }
        });
    }

}
