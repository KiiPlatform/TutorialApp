package com.kii.android.sdk.tutorial;

public class DetailDialogResource {
    private String title;
    private String detail;
    private int imageId;
    private String docsUrl;

    public DetailDialogResource(String titleId, String detailId) {
        super();
        this.title = titleId;
        this.detail = detailId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDocsUrl() {
        return docsUrl;
    }

    public void setDocsUrl(String docsUrl) {
        this.docsUrl = docsUrl;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    

}
