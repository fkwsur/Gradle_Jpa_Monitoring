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
@Table(name = "site")
public class Site extends BaseEntity {
    @Id
    private String id;
    @Column(name = "site_name")
    @JsonProperty("site_name")
    private String siteName;
    private String address;
    private String number;
    private String company;
    @Column(name = "site_logo")
    @JsonProperty("site_logo")
    private String siteLogo;
}
