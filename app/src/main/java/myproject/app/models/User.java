package myproject.app.models;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    private String id;
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("app_key")
    @Column(name = "app_key")
    private String appKey;
    private String name;
    private String email;
    private String number;
    @Column(name = "site_id")
    @JsonProperty("site_id")
    private String siteId;
    private String department;
    private String position;
    private String grade;
    private String refreshtoken;
}
