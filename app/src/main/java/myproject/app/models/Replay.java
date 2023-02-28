package myproject.app.models;


import java.time.LocalDateTime;

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
@Table(name = "replay")
public class Replay extends BaseEntity {
    @Id
    private String id;
    @Column(name = "site_id")
    @JsonProperty("site_id")
    private String siteId;
    @Column(name = "time", updatable = false)
    private LocalDateTime time;
    private String msg;
    private String degree;
    @Column(name = "video_url")
    @JsonProperty("video_url")
    private String videoUrl;
}
