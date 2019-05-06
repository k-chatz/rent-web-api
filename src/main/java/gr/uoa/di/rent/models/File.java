package gr.uoa.di.rent.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
        "filesize"
})
public class File implements Serializable {

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

    public File() {
    }

    public File(Long uploader_id, String filename, String filetype, long filesize) {
        this.uploader_id = uploader_id;
        this.filename = filename;
        this.filetype = filetype;
        this.filesize = filesize;
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

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", uploader=" + uploader +
                ", uploader_id=" + uploader_id +
                ", filename='" + filename + '\'' +
                ", filetype='" + filetype + '\'' +
                ", filesize=" + filesize +
                '}';
    }
}
