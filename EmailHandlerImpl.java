package vn.com.vpbanks.service.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import vn.com.vpbanks.configs.properties.AttachmentProperties;
import vn.com.vpbanks.constants.AttachmentMapping;
import vn.com.vpbanks.constants.AttachmentTemplate;
import vn.com.vpbanks.constants.MailTemplate;
import vn.com.vpbanks.constants.enums.MailAttachment;
import vn.com.vpbanks.dto.mail.FlexMailMessage;
import vn.com.vpbanks.dto.mail.attachment.*;
import vn.com.vpbanks.dto.sms.KafkaNotificationMessage;
import vn.com.vpbanks.dto.store.InputMR0030DTO;
import vn.com.vpbanks.dto.store.InputMR0031DTO;
import vn.com.vpbanks.dto.store.InputT0214DTO;
import vn.com.vpbanks.dto.store.InputT0215DTO;
import vn.com.vpbanks.repository.EmailDAO;
import vn.com.vpbanks.service.kafka.EmailHandler;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static vn.com.vpbanks.constants.FlexTopics.*;
import static vn.com.vpbanks.constants.enums.SendNoticationFlexType.EMAIL;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailHandlerImpl implements EmailHandler {
    private static final List<String> attachmentEmails = List.of("T0214", "T0215", "T028E", "T029E", "T030E", "T031E", "T032E");
    private final ObjectMapper objectMapper;
    private final EmailDAO emailDAO;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AttachmentProperties attachmentProperties;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = FLEX_EMAIL_TOPIC)
    @Transactional(rollbackFor = JsonProcessingException.class)
    public void receiveEmailMessage(@Header(KafkaHeaders.OFFSET) Integer offset, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition, @Payload String message) {
        log.info("Received message from  partition = {} ,offset = {}, message {}  ", partition, offset, message);
        FlexMailMessage flexSmsMessage;
        try {
            flexSmsMessage = objectMapper.readValue(message, FlexMailMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Have error during convert json to object {}", e.getMessage());
            throw new ApiException(e);
        }

        if (attachmentEmails.contains(flexSmsMessage.getTemplateId())) {
            this.handleMailAttachmentFile(flexSmsMessage);
        } else {
            this.handleMailNoAttachment(flexSmsMessage);
        }
    }

    private void handleMailNoAttachment(FlexMailMessage flexMailMessage) {
        Optional<MailTemplate> mailTemplate = MailTemplate.getTemplate(flexMailMessage.getTemplateId());
        if (mailTemplate.isEmpty()) {
            log.info("Unknown templateId {} ", flexMailMessage.getTemplateId());
            return;
        }
        KafkaNotificationMessage kafkaNotificationMessage = this.buildKafkaNotification(flexMailMessage, mailTemplate.get());
        log.info("[Flex Connector] send email message:  {} to base service ", kafkaNotificationMessage);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(NOTIFICATION_EMAIL_TOPIC, kafkaNotificationMessage);
        future.completable().whenComplete((success, failed) -> {
            if (failed != null) {
                emailDAO.updateEmailLog(new BigDecimal(flexMailMessage.getAutoId()), "E", failed.getMessage());
            } else {
                emailDAO.updateEmailLog(new BigDecimal(flexMailMessage.getAutoId()), "S", "FC sent message successfully");
                log.debug("Updated email log");
            }
        });
    }

    private void handleMailAttachmentFile(FlexMailMessage flexMailMessage) {
        Optional<MailTemplate> mailTemplate = MailTemplate.getTemplate(flexMailMessage.getTemplateId());
        if (mailTemplate.isEmpty()) {
            log.info("Unknown templateId {} ", flexMailMessage.getTemplateId());
            return;
        }
        KafkaNotificationMessage kafkaNotificationMessage = this.buildKafkaNotification(flexMailMessage, mailTemplate.get());
        kafkaNotificationMessage.setAccountNo(this.getValueByFieldName(kafkaNotificationMessage.getData(), CUSTOMER_CODE));
        var attachmentMailTemplate = AttachmentMapping.getTemplate(flexMailMessage.getTemplateId());
        List<Pair<String, Object>> attachmentFiles = new ArrayList<>();
        if (attachmentMailTemplate.isEmpty()) {
            log.info("Not have attachment with templateId {} ", flexMailMessage.getTemplateId());
        } else {
            AttachmentMapping attachmentMapping = attachmentMailTemplate.get();
            attachmentMapping.getAttachments().forEach(attachmentCode -> {
                List<Object> attachmentFile = getAttachmentData(attachmentCode, flexMailMessage.getTemplateId(), kafkaNotificationMessage.getData());
                if (attachmentFile != null && attachmentFile.size() > 0)
                    attachmentFiles.add(Pair.of(attachmentCode, attachmentFile.get(0)));
            });
            kafkaNotificationMessage.setAttachment(attachmentFiles);
            log.info("[Flex Connector] send email message:  {} to base service ", kafkaNotificationMessage);
        }

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(NOTIFICATION_EMAIL_TOPIC, kafkaNotificationMessage);
        future.completable().whenComplete((success, failed) -> {
            if (failed != null) {
                emailDAO.updateEmailLog(new BigDecimal(flexMailMessage.getAutoId()), "E", failed.getMessage());
            } else {
                emailDAO.updateEmailLog(new BigDecimal(flexMailMessage.getAutoId()), "S", "FC sent message successfully");
                log.debug("Updated email log");
            }
        });
    }

    private List<Object> getAttachmentData(String attachmentCode, String templateId, Object inputData) {
        Optional<AttachmentTemplate> attachmentTemplate = AttachmentTemplate.getAttachmentTemplate(attachmentCode);
        if (attachmentTemplate.isEmpty()) {
            log.info("Unknown attachmentTemplate Code {} ", attachmentCode);
            return new ArrayList<>();
        }
        Class<?> clazz = attachmentTemplate.get().getClazz();

        switch (MailAttachment.valueOf(templateId)) {
            case T0214:
                return Collections.singletonList(emailDAO.getT0214(this.buildInputT0214((T0214Message) inputData), this.getStoreProcedure(attachmentCode), clazz));
            case T0215:
                return Collections.singletonList(emailDAO.getT0215(this.buildInputT0215((T0215Message) inputData), this.getStoreProcedure(attachmentCode), clazz));
            case T028E:
                return Collections.singletonList(emailDAO.getMr0030(InputMR0030DTO.buildInputMr0030DTO((T028EMessage) inputData)));
            case T029E:
                return Collections.singletonList(emailDAO.getMr0030(InputMR0030DTO.buildInputMr0030DTO((T029EMessage) inputData)));
            case T030E:
                return Collections.singletonList(emailDAO.getMr0030(InputMR0030DTO.buildInputMr0030DTO((T030EMessage) inputData)));
            case T031E:
                return Collections.singletonList(emailDAO.getMr0031(InputMR0031DTO.buildInputMr0031DTO((T031EMessage) inputData)));
            case T032E:
                return Collections.singletonList(emailDAO.getMr0031(InputMR0031DTO.buildInputMr0031DTO((T032EMessage) inputData)));
            default:
                return new ArrayList<>();
        }
    }


    private KafkaNotificationMessage buildKafkaNotification(FlexMailMessage flexMailMessage, MailTemplate
            mailTemplate) {
        Object message = emailDAO.retrieveData(flexMailMessage.getQuery(), mailTemplate.getClazz());
        log.info("Message: {}", message);
        KafkaNotificationMessage result =  KafkaNotificationMessage.builder()
                .autoId(PREFIX_SMSEMAIL_TOPIC + flexMailMessage.getAutoId() + "_" + UUID.randomUUID())
                .emailOrPhone(flexMailMessage.getEmail()).type(EMAIL)
                .templateId(flexMailMessage.getTemplateId())
                .data(message)
                .build();

        try {
            String data = getValueByFieldName(message, "emailtp");
            result.setCcEmails(Arrays.asList(data.split(",")));
            return result;
        } catch (Exception e) {
            log.error("Exception e : {}", e.getMessage());
            return result;
        }
    }

    private String getStoreProcedure(String attachmentCode) {
        AttachmentTemplate attachmentTemplate = AttachmentTemplate.valueOf(attachmentCode);
        log.info("Check config attachmentTemplate: {} ", attachmentProperties.toString());
        switch (attachmentTemplate) {
            case CFD008:
                return attachmentProperties.getAttachmentT0214().getCFD008();
            case CFD0081:
                return attachmentProperties.getAttachmentT0214().getCFD0081();
            case CFD0082:
                return attachmentProperties.getAttachmentT0214().getCFD0082();
            case CFD0085:
                return attachmentProperties.getAttachmentT0214().getCFD0085();
            case CFD0086:
                return attachmentProperties.getAttachmentT0214().getCFD0086();
            case OD0001:
                return attachmentProperties.getAttachmentT0215().getOD0001();
            default:
                return null;
        }

    }

    private String getValueByFieldName(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            return (String) value;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    private InputT0214DTO buildInputT0214(T0214Message message) {
        return InputT0214DTO.builder()
                .opt("A")
                .brid("0001")
                .tlGroups(null)
                .tlScope("A")
                .iDate(message.getI_DATE())
                .custoDyCd(message.getPV_CUSTODYCD())
                .afAcctNo(message.getPV_AFACCTNO())
                .afType(message.getPV_AFTYPE())
                .build();

    }

    private InputT0215DTO buildInputT0215(T0215Message message) {
        return InputT0215DTO.builder()
                .opt("A")
                .brid("0001")
                .tlGroups(null)
                .tlScope("A")
                .fDate(message.getF_DATE())
                .tDate(message.getT_DATE())
                .custoDyCd(message.getPV_CUSTODYCD())
                .afAcctNo(message.getPV_AFACCTNO())
                .execType(message.getEXECTYPE())
                .symbol(message.getSYMBOL())
                .tlid("6868")
                .build();

    }
}
