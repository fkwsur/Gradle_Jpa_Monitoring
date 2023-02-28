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
public class ExcelCarLiftLog  {
   

    @Field(value = "Site_Name")
    @JsonProperty("Site_Name")
    private String siteName;

  
    @Field(value = "Equip")
    @JsonProperty("Equip")
    private String equip;


    @Field(value = "Plate_Num")
    @JsonProperty("Plate_Num")
    private String plateNum;

 
    @Field(value = "Time")
    @JsonProperty("Time")
    private String time;

  
}