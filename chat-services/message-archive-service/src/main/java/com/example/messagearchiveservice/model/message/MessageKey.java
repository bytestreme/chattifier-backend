package com.example.messagearchiveservice.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@PrimaryKeyClass
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageKey {

    @PrimaryKeyColumn(name = "id", ordinal = 1, ordering = DESCENDING)
    private UUID id;

    @PrimaryKeyColumn(name = "timestamp")
    @NonNull
    private Long timestamp;

    @PrimaryKeyColumn(name = "chat_id", type = PARTITIONED)
    @NonNull
    private String chatId;

    public static MessageKey create(Long timestamp, String chatId) {
        return new MessageKey(UUID.randomUUID(), timestamp, chatId);
    }

}
