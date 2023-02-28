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
@Table(name = "site_cctv")
public class SiteCctv extends BaseEntity {
    @Id
    private String id;
    @Column(name = "site_id")
    @JsonProperty("site_id")
    private String siteId;
    @Column(name = "cctv_url")
    @JsonProperty("cctv_url")
    private String cctvUrl;
    @Column(name = "cctv_nickname")
    @JsonProperty("cctv_nickname")
    private String cctvNickname;
    @Column(name = "hls_url_realtime")
    @JsonProperty("hls_url_realtime")
    private String hlsUrlRealtime;
    @Column(name = "hls_url_streaming")
    @JsonProperty("hls_url_streaming")
    private String hlsUrlStreaming;
}
