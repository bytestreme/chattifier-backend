package com.example.messagearchiveservice.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
