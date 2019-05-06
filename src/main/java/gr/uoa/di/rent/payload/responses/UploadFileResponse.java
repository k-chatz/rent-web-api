package gr.uoa.di.rent.payload.responses;

import gr.uoa.di.rent.models.File;

public class UploadFileResponse {

    private long id;
    private Long uploader_id;
    private String fileName;
    private String fileType;
    private long filesize;
    private String fileDownloadUri;

    public UploadFileResponse() {
    }

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long filesize) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.filesize = filesize;
    }

    public UploadFileResponse(File file) {

        this.id = file.getId();
        this.uploader_id = file.getUploader_id();
        this.fileName = file.getFilename();
        this.fileType = file.getFiletype();
        this.filesize = file.getFilesize();
        this.fileDownloadUri = file.getFileDownloadUri();
    }

    public long getId() {
        return id;
    }

    public long setId() {
        return id;
    }

    public Long getUploader_id() {
        return uploader_id;
    }

    public void setUploader_id(Long uploader_id) {
        this.uploader_id = uploader_id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        return "UploadFileResponse{" +
                "id=" + id +
                ", uploader_id=" + uploader_id +
                ", fileName='" + fileName + '\'' +
                ", fileDownloadUri='" + fileDownloadUri + '\'' +
                ", fileType='" + fileType + '\'' +
                ", filesize=" + filesize +
                '}';
    }
}
