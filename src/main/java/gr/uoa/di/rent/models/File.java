package gr.uoa.di.rent.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gr.uoa.di.rent.models.audit.UserDateAudit;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "files", schema = "rent")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "uploader_id",
        "filename",
        "filetype",
        "filesize",
        "fileDownloadUri"
})
public class File extends UserDateAudit implements Serializable {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploader", nullable = false)
    @JsonIgnore
    private User uploader;

    @Transient
    @JsonProperty("uploader_id")
    private Long uploader_id;

    @Column(name = "filename", nullable = false, length = 255)
    @JsonProperty("filename")
    private String filename;

    @Column(name = "filetype", nullable = false, length = 20)
    @JsonProperty("filetype")
    private String filetype;

    @Column(name = "filesize", nullable = false)
    @JsonProperty("filesize")
    private long filesize;

    @Column(name = "fileDownloadUri", nullable = false)
    @JsonProperty("fileDownloadUri")
    private String fileDownloadUri;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel", nullable = false)
    @JsonProperty("hotel")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room", nullable = false)
    @JsonProperty("room")
    private Room room;

    public File() {
    }

    public File(User uploader, String filename, String filetype, long filesize, String fileDownloadUri) {
        this.setUploader(uploader);
        this.filename = filename;
        this.filetype = filetype;
        this.filesize = filesize;
        this.fileDownloadUri = fileDownloadUri;
    }

    public long getId() {
        return id;
    }

    public long setId() {
        return id;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
        this.setUploader_id(uploader.getId());
    }

    public Long getUploader_id() {
        return uploader_id;
    }

    public void setUploader_id(Long uploader_id) {
        this.uploader_id = uploader_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", uploader=" + uploader +
                ", uploader_id=" + uploader_id +
                ", filename='" + filename + '\'' +
                ", filetype='" + filetype + '\'' +
                ", filesize=" + filesize +
                ", fileDownloadUri='" + fileDownloadUri + '\'' +
                '}';
    }
}
