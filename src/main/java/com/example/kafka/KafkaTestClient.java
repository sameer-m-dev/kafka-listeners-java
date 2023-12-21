package com.example.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.Node;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Collection;

public class KafkaTestClient {
    private static final String TOPIC = "test_topic";

    public static void main(String[] args) {
        String bootstrapServer = args.length > 0 ? args[0] : "localhost:9092";
        System.out.println("🥾 Bootstrap server: " + bootstrapServer);

        Properties adminProps = new Properties();
        adminProps.put("bootstrap.servers", bootstrapServer);

        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            DescribeClusterResult clusterResult = adminClient.describeCluster();
            Collection < Node > nodes = clusterResult.nodes().get();
            System.out.println("\n✅ Connected to bootstrap server(" + bootstrapServer + ") and it returned metadata for brokers listed below:\n");

            for (Node node: nodes) {
                System.out.println("👉 Broker ID: " + node.id() + ", Host: " + node.host() + ", Port: " + node.port());
            }

            // Additional information goes here
            System.out.println("\n---------------------\n" +
                "ℹ️  This step confirms the successful bootstrap connection and provides broker metadata required for consumer resolution.\n" +
                "ℹ️  Ensure your client can resolve the broker(s) shown in the metadata above.\n" +
                "ℹ️  If the listed host(s) are inaccessible from your client, consider adjusting the advertised.listener configuration on Kafka broker(s).\n");

            produceMessage(bootstrapServer);
            consumeMessage(bootstrapServer);
        } catch (Exception e) {
            System.out.println("\n❌ Failed to connect to bootstrap server.\n" +
                "👉 " + e.getMessage() + "\n" +
                "ℹ️  Check that Kafka is running, and that the bootstrap server you've provided (" + bootstrapServer + ") is reachable from your client.\n");
        }
    }

    private static void produceMessage(String bootstrapServer) {
        System.out.println("\n<Producing>");
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", bootstrapServer);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer < String, String > producer = new KafkaProducer < > (producerProps)) {
            producer.send(new ProducerRecord < > (TOPIC, "foo / " + java.time.LocalDateTime.now()), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.out.println("❌ Message delivery failed: " + exception.getMessage());
                    } else {
                        System.out.println("✅  📬  Message delivered: \"" + metadata + "\" to " +
                            metadata.topic() + " [partition " + metadata.partition() + "]");
                    }
                }
            });
            producer.flush();
        } catch (Exception e) {
            System.out.println("❌ (uncaught exception in produce): " + e.getMessage());
        }
    }

    private static void consumeMessage(String bootstrapServer) {
        System.out.println("\n<Consuming>");
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", bootstrapServer);
        consumerProps.put("group.id", "rmoff");
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (Consumer < String, String > consumer = new KafkaConsumer < > (consumerProps)) {
            consumer.subscribe(Collections.singletonList(TOPIC));
            ConsumerRecords<String, String> messages = consumer.poll(Duration.ofMillis(10000));

            if (messages.isEmpty()) {
                System.out.println("❌ No message(s) consumed (maybe we timed out waiting?)\n");
            } else {
                messages.forEach(record ->
                    System.out.println("✅  💌  Message received: \"" + record.value() +
                        "\" from topic " + record.topic() + "\n")
                );
            }
        } catch (Exception e) {
            System.out.println("❌ Consumer error: " + e.getMessage() + "\n");
        }
    }
}