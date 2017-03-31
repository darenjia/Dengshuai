package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by BKJCB on 2017/3/21.
 */

public class CheckResult {

    private int id;  //主键id
    private int identifier; //唯一标识符，用于确定属于哪个检查计划
    private int num;   //结果编号
    private String comment; //文字备注信息
    private int result;  //选择结果
    private String imageUrls; //图片文件地址，多张以，号隔开
    private String videoUrls; //视屏文件地址
    private String audioUrls; //录音文件地址

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
