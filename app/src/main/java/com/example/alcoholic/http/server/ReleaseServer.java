package com.example.alcoholic.http.server;

import com.hjq.http.config.IRequestServer;

/**
 * Created by
 * Description:
 * on 2020/11/13.
 */
public class ReleaseServer implements IRequestServer {
    @Override
    public String getHost() {
        return "https://www.baidu.com/";
    }

    @Override
    public String getPath() {
        return "api/";
    }
}
