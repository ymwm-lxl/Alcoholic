package com.example.alcoholic.http.request;

import com.hjq.http.annotation.HttpIgnore;
import com.hjq.http.config.IRequestApi;

import java.io.File;

/**
 * Created by
 * Description: 图片上传接口
 * on 2020/11/23.
 */
public class ImgUpLoadApi implements IRequestApi {
    @Override
    public String getApi() {
        return "1/upload";
    }


    private String key = "ce2334330e0e82e325cb0f2aecc2e66a";
    private File image;
    private String name;
    private long expiration;


    public ImgUpLoadApi setKey(String key) {
        this.key = key;
        return this;
    }

    public ImgUpLoadApi setImage(File image) {
        this.image = image;
        return this;
    }

    public ImgUpLoadApi setName(String name) {
        this.name = name;
        return this;
    }

    public ImgUpLoadApi setExpiration(long expiration) {
        this.expiration = expiration;
        return this;
    }
}
