package com.example.messagedeliveryservice.model.repository.message;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("messages")
public class MessageTable {

    @PrimaryKey
    private MessageKey key;

    @NonNull
    private String text;

    @NonNull
    private String sender;

    public static MessageTable create(MessageKey messageKey, String text, String sender) {
        return new MessageTable(messageKey, text, sender);
    }

}
