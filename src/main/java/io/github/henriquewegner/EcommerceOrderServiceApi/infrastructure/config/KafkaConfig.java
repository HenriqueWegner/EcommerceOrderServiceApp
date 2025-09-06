package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.config;

import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.PaymentEvent;
import io.github.henriquewegner.EcommerceOrderServiceApi.domain.event.ShippingEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate,
                                            @Value("${spring.kafka.topic.payment-update-dlq}") String paymentDlq,
                                            @Value("${spring.kafka.topic.shipping-update-dlq}") String shippingDlq) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> {
                    String dlqTopic;
                    switch (record.topic()) {
                        case "payment-updates":
                            dlqTopic = paymentDlq;
                            break;
                        case "shipping-updates":
                            dlqTopic = shippingDlq;
                            break;
                        default:
                            dlqTopic = record.topic() + "-dlq";
                    }
                    return new TopicPartition(dlqTopic, record.partition());
                }
        );

        return new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0));
    }

    private Map<String, Object> baseProps(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    @Bean
    public ConsumerFactory<String, PaymentEvent> paymentConsumerFactory() {
        JsonDeserializer<PaymentEvent> jd = new JsonDeserializer<>(PaymentEvent.class);
        jd.addTrustedPackages("io.github.henriquewegner.EcommerceOrderServiceApi.domain.event");
        jd.ignoreTypeHeaders(); // ✅ não exige __TypeId__ nos headers
        return new DefaultKafkaConsumerFactory<>(
                baseProps("payment-group"),
                new StringDeserializer(),
                jd
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentKafkaListenerFactory(
            DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ShippingEvent> shippingConsumerFactory() {
        JsonDeserializer<ShippingEvent> jd = new JsonDeserializer<>(ShippingEvent.class);
        jd.addTrustedPackages("io.github.henriquewegner.EcommerceOrderServiceApi.domain.event");
        jd.ignoreTypeHeaders();
        return new DefaultKafkaConsumerFactory<>(
                baseProps("shipping-group"),
                new StringDeserializer(),
                jd
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShippingEvent> shippingKafkaListenerFactory(
            DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ShippingEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(shippingConsumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}

