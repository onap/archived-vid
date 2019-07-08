package org.onap.simulator.db.entities;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "fn_app")
public class App {
    @Id
    @Column(name = "app_id")
    protected Integer id;
    @Column(name = "app_name")
    private String name; // app_name
    @Column(name = "app_image_url")
    private String imageUrl; // app_image_url
    @Column(name = "app_description")
    private String description; // app_description
    @Column(name = "app_notes")
    private String notes; // app_notes
    @Column(name = "app_url")
    private String url; // app_url
    @Column(name = "app_alternate_url")
    private String alternateUrl; // app_alternate_url
    @Column(name = "app_rest_endpoint")
    private String restEndpoint; // app_rest_endpoint
    @Column(name = "ml_app_name")
    private String mlAppName; // ml_app_name
    @Column(name = "ml_app_admin_id")
    private String mlAppAdminId; // ml_app_admin_id
    @Column(name = "mots_id")
    private Integer motsId; // mots_id
    @Column(name = "app_password")
    private String appPassword; // app_password
    @Column(columnDefinition = "varchar")
    @Type(type="yes_no")
    private Boolean open;
    @Column(columnDefinition = "varchar")
    @Type(type="yes_no")
    private Boolean enabled;
    @Column(columnDefinition="mediumblob")
    private byte[] thumbnail;
    @Column(name = "app_username")
    private String username; // app_username
    @Column(name = "ueb_key")
    private String uebKey; // ueb_key
    @Column(name = "ueb_secret")
    private String uebSecret; // ueb_secret
    @Column(name = "ueb_topic_name")
    private String uebTopicName; // ueb_topic_name


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlternateUrl() {
        return alternateUrl;
    }

    public void setAlternateUrl(String alternateUrl) {
        this.alternateUrl = alternateUrl;
    }

    public String getRestEndpoint() {
        return restEndpoint;
    }

    public void setRestEndpoint(String restEndpoint) {
        this.restEndpoint = restEndpoint;
    }

    public String getMlAppName() {
        return mlAppName;
    }

    public void setMlAppName(String mlAppName) {
        this.mlAppName = mlAppName;
    }

    public String getMlAppAdminId() {
        return mlAppAdminId;
    }

    public void setMlAppAdminId(String mlAppAdminId) {
        this.mlAppAdminId = mlAppAdminId;
    }

    public Integer getMotsId() {
        return motsId;
    }

    public void setMotsId(Integer motsId) {
        this.motsId = motsId;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUebKey() {
        return uebKey;
    }

    public void setUebKey(String uebKey) {
        this.uebKey = uebKey;
    }

    public String getUebSecret() {
        return uebSecret;
    }

    public void setUebSecret(String uebSecret) {
        this.uebSecret = uebSecret;
    }

    public String getUebTopicName() {
        return uebTopicName;
    }

    public void setUebTopicName(String uebTopicName) {
        this.uebTopicName = uebTopicName;
    }
}
