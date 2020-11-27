package com.example.alcoholic.http.server;

import com.hjq.http.config.IRequestServer;

/**
 * Created by
 * Description:github
 * on 2020/11/26.
 */
//https://raw.githubusercontent.com/ymwm-lxl/Alcoholic/master/up_load_txt.json
public class GithubServer implements IRequestServer {
    @Override
    public String getHost() {
        return "https://github.com/ymwm-lxl/Alcoholic/";
    }
}
