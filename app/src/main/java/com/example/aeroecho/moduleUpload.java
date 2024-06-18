package com.example.aeroecho;

public class moduleUpload {
    private String id;
    private String fileUrl;

    public moduleUpload() {
        // Default constructor required for calls to DataSnapshot.getValue(Module.class)
    }

    public moduleUpload(String id, String fileUrl) {
        this.id = id;
        this.fileUrl = fileUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
