package myproject.app.models;

import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.mongodb.core.mapping.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "chattings")
public class Chatting {
    @Id
    @Field(value = "_id")
    @JsonProperty("_id")
    private String id;

    @Field(value = "name")
    @JsonProperty("name")
    private String name;

    @Field(value = "roomName")
    @JsonProperty("roomCode")
    private String roomName;

    @Field(value = "message")
    @JsonProperty("message")
    private String message;

    @Field(value = "time")
    @JsonProperty("time")
    private String time;
}
