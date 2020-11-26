package com.example.alcoholic.http.server;

import com.hjq.http.config.IRequestServer;

/**
 * Created by
 * Description: imgbb fuwu
 * on 2020/11/23.
 */
public class ImgBBServer implements IRequestServer {
    @Override
    public String getHost() {
        return "https://api.imgbb.com/";
    }
}
