package com.example.alcoholic.bean;

/**
 * Created by
 * Description:版本更新实体类
 * on 2020/11/25.
 */
public class UpVersionBean {

    /***
     * {
     *     "minMustUpCode":1,
     *     "vAPKDownUrl":"www.www",
     *     "vCode":1,
     *     "vName":"1.0",
     *     "vUpContent":"更心新的版本"
     * }
     */

    /* 版本名称 */
    private String vName;
    /* 版本号 */
    private int vCode;
    /* 最小强制更新版本 */
    private int minMustUpCode;
    /* apk下栽链接 */
    private String vAPKDownUrl;
    /* 更新说明 */
    private String vUpContent;

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public int getvCode() {
        return vCode;
    }

    public void setvCode(int vCode) {
        this.vCode = vCode;
    }

    public int getMinMustUpCode() {
        return minMustUpCode;
    }

    public void setMinMustUpCode(int minMustUpCode) {
        this.minMustUpCode = minMustUpCode;
    }

    public String getvAPKDownUrl() {
        return vAPKDownUrl;
    }

    public void setvAPKDownUrl(String vAPKDownUrl) {
        this.vAPKDownUrl = vAPKDownUrl;
    }

    public String getvUpContent() {
        return vUpContent;
    }

    public void setvUpContent(String vUpContent) {
        this.vUpContent = vUpContent;
    }
}
