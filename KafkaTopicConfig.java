package vn.com.vpbanks.flex.usecase.service.common.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import vn.com.vpbanks.flex.usecase.service.common.constants.KafkaTopicConstants;

import java.util.HashMap;
import java.util.Map;

import static vn.com.vpbanks.flex.usecase.service.common.constants.KafkaTopicConstants.*;

@Configuration
public class KafkaTopicConfig {
    @Value("${vpbanks.kafka.server}")
    private String bootstrapServer;

    @Value("${vpbanks.kafka.properties.topics.flex.orders}")
    private String orderTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.cancelOrders}")
    private String cancelOrders;

    @Value("${vpbanks.kafka.properties.topics.flex.ordersResult}")
    private String ordersResult;

    @Value("${vpbanks.kafka.properties.topics.flex.cancelOrdersResult}")
    private String cancelOrdersResult;

    @Value("${vpbanks.kafka.properties.topics.flex.orderNotification}")
    private String orderNotification;

    @Value("${vpbanks.kafka.properties.topics.flex.cashNotification}")
    private String cashNotificationTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.accountNotification}")
    private String accountNotificationTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.acctNoNotification}")
    private String acctNoNotificationTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.smartOtpNotificationTopic}")
    private String smartOtpNotificationTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.openAccountVsdNotificationTopic}")
    private String openAccountVsdNotificationTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.signalChangePasswordTopic}")
    private String signalChangePasswordTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.signalSyncSETopic}")
    private String signalSyncSETopic;

    @Value("${vpbanks.kafka.properties.topics.flex.signalSyncCATopic}")
    private String signalSyncCATopic;

    @Value("${vpbanks.kafka.properties.topics.flex.signalBatch1Topic}")
    private String signalBatch1Topic;

    @Value("${vpbanks.kafka.properties.topics.flex.signalEODTopic}")
    private String signalEODTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.signalEInvestTopic}")
    private String signalEInvestTopic;

    @Value("${vpbanks.kafka.properties.topics.flex.orderCash}")
    private String orderCash;

    @Value("${vpbanks.kafka.properties.topics.flex.orderMargin}")
    private String orderMargin;

    @Value("${vpbanks.kafka.properties.topics.flex.orderCopyTrade}")
    private String orderCopyTrade;

    @Value("${vpbanks.kafka.properties.topics.flex.orderSaleSupport}")
    private String orderSaleSupport;

    @Value("${vpbanks.kafka.properties.topics.flex.orderAll}")
    private String orderAll;

    @Value("${vpbanks.kafka.properties.topics.flex.customerCare}")
    private String customerCare;

    @Value("${vpbanks.kafka.properties.topics.flex.notifyQueue}")
    private String notifyQueue;

    @Value("${vpbanks.kafka.properties.topics.flex.conditionOrder}")
    private String conditionOrder;

    @Value("${vpbanks.kafka.properties.topics.flex.postConditionOrderResult}")
    private String postConditionOrderResult;

    @Value("${vpbanks.kafka.properties.topics.flex.cancelConditionOrderResult}")
    private String cancelConditionOrderResult;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic postOrder() {
        return new NewTopic(orderTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic cancelOrder() {
        return new NewTopic(cancelOrders, 5, (short) 1);
    }

    @Bean
    public NewTopic ordersResult() {
        return new NewTopic(ordersResult, 5, (short) 1);
    }

    @Bean
    public NewTopic cancelOrdersResult() {
        return new NewTopic(cancelOrdersResult, 5, (short) 1);
    }

    @Bean
    public NewTopic orderNotification() {
        return new NewTopic(orderNotification, 5, (short) 1);
    }

    @Bean
    public NewTopic cashNotificationTopic() {
        return new NewTopic(cashNotificationTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic accountNotification() {
        return new NewTopic(accountNotificationTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic acctNoNotification() {
        return new NewTopic(acctNoNotificationTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic smartOtpNotification() {
        return new NewTopic(smartOtpNotificationTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic openAccountVsdNotificationTopic() {
        return new NewTopic(openAccountVsdNotificationTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic orderCash() {
        return new NewTopic(orderCash, 5, (short) 1);
    }

    @Bean
    public NewTopic orderMargin() {
        return new NewTopic(orderMargin, 5, (short) 1);
    }

    @Bean
    public NewTopic orderCopyTrade() {
        return new NewTopic(orderCopyTrade, 5, (short) 1);
    }

    @Bean
    public NewTopic orderSaleSupport() {
        return new NewTopic(orderSaleSupport, 5, (short) 1);
    }

    @Bean
    public NewTopic orderAll() {
        return new NewTopic(orderAll, 5, (short) 1);
    }

    @Bean
    public NewTopic customerCare() {
        return new NewTopic(customerCare, 5, (short) 1);
    }

    @Bean
    public NewTopic notifyQueue() {
        return new NewTopic(notifyQueue, 5, (short) 1);
    }


    @Bean
    public NewTopic signalChangePasswordTopic() {
        return new NewTopic(signalChangePasswordTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic signalSyncSETopic() {
        return new NewTopic(signalSyncSETopic, 5, (short) 1);
    }

    @Bean
    public NewTopic signalSyncCATopic() {
        return new NewTopic(signalSyncCATopic, 5, (short) 1);
    }

    @Bean
    public NewTopic signalBatch1Topic() {
        return new NewTopic(signalBatch1Topic, 5, (short) 1);
    }

    @Bean
    public NewTopic signalEODTopic() {
        return new NewTopic(signalEODTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic conditionOrder() {
        return new NewTopic(conditionOrder, 5, (short) 1);
    }

    @Bean
    public NewTopic cancelConditionOrderResult() {
        return new NewTopic(cancelConditionOrderResult, 5, (short) 1);
    }

    @Bean
    public NewTopic postConditionOrderResult() {
        return new NewTopic(postConditionOrderResult, 5, (short) 1);
    }

    @Bean
    public NewTopic signalEInvestTopic() {
        return new NewTopic(signalEInvestTopic, 5, (short) 1);
    }

    @Bean
    public NewTopic signalignalOpenBondAcctIamTopic() {
        return new NewTopic(FLEX_SIGNAL_OPEN_BOND_ACCT_IAM_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic signalSessionTopic() {
        return new NewTopic(FLEX_SIGNAL_SESSION_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic signalCompCodBondTopic() {
        return new NewTopic(FLEX_SIGNAL_COMP_COD_BOND_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic postOrderCwTopic() {
        return new NewTopic(FLEX_POST_ORDER_CW_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic postOrderResultCwTopic() {
        return new NewTopic(FLEX_POST_ORDER_RESULT_CW_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic cancelOrderCwTopic() {
        return new NewTopic(FLEX_CANCEL_ORDER_CW_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic cancelOrderCwResultTopic() {
        return new NewTopic(FLEX_CANCEL_ORDER_CW_RESULT_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic OrderResultCwTopic() {
        return new NewTopic(FLEX_SIGNAL_ORDER_RESULT_CW_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic notifySmsTopic() {
        return new NewTopic(KafkaTopicConstants.FLEX_SMS_TOPIC, 5, (short) 1);
    }

    @Bean
    public NewTopic notifyMailNoAttachmentTopic() {
        return new NewTopic(KafkaTopicConstants.FLEX_EMAIL_TOPIC, 5, (short) 1);
    }
}
