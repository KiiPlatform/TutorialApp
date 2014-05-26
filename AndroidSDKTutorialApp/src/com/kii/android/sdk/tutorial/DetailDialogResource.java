package com.kii.android.sdk.tutorial;

public class DetailDialogResource {
    private String title;
    private String detail;
    private int imageId;

    public DetailDialogResource(String titleId, String detailId, int imageId) {
        super();
        this.title = titleId;
        this.detail = detailId;
        this.imageId = imageId;
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

}
