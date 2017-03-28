package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by BKJCB on 2017/3/21.
 */

public class CheckResult {

    private int id;
    private int identifier;
    private int num;
    private String comment;
    private int result;
    private String imageUrls;
    private String videoUrls;
    private String audioUrls;

    public CheckResult() {
    }

    public int getId() {
        return id;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(String videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getAudioUrls() {
        return audioUrls;
    }

    public void setAudioUrls(String audioUrls) {
        this.audioUrls = audioUrls;
    }
}
