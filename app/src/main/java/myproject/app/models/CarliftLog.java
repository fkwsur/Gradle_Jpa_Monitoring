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
@Document(collection = "carlift_logs")
public class CarliftLog  {
    @Id
    @Field(value = "_id")
    @JsonProperty("_id")
    private String id;

    @Field(value = "Site_Name")
    @JsonProperty("Site_Name")
    private String siteName;

    @Field(value = "CarLift")
    @JsonProperty("CarLift")
    private String carLift;

    @Field(value = "Equip")
    @JsonProperty("Equip")
    private String equip;

    @Field(value = "Gate")
    @JsonProperty("Gate")
    private String gate;

    @Field(value = "Plate_Num")
    @JsonProperty("Plate_Num")
    private String plateNum;

    @Field(value = "Position")
    @JsonProperty("Position")
    private String position;

    @Field(value = "Repres")
    @JsonProperty("Repres")
    private String repres;

    @Field(value = "Time")
    @JsonProperty("Time")
    private String time;

    @Field(value = "Alarm")
    @JsonProperty("Alarm")
    private List<AlarmEntity> alarm;

    public class AlarmEntity {
        @Field(value = "cam_name")
        @JsonProperty("cam_name")
        private String camName;
        @Field(value = "degree")
        @JsonProperty("degree")
        private String degree;
        @Field(value = "msg")
        @JsonProperty("msg")
        private String msg;
    }
}
