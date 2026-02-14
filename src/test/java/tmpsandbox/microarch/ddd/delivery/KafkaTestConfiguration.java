package tmpsandbox.microarch.ddd.delivery;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

@TestConfiguration
public class KafkaTestConfiguration {

    @Bean
    public Consumer<String, byte[]> ordersEventsConsumer(
        @Value("${spring.kafka.bootstrap-servers}")
        String bootstrapServers
    ) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        Consumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("orders.events"));

        return consumer;
    }
}
