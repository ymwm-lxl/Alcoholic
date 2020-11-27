package com.example.alcoholic.http.request;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.annotation.HttpRename;
import com.hjq.http.config.IRequestApi;

/**
 * Created by
 * Description:
 * on 2020/11/26.
 */
public class GithubUpLoadApi implements IRequestApi {
    @Override
    public String getApi() {
        return "blob/master/up_load_txt.json";
    }

    @HttpHeader
    @HttpRename("Content-Type")
    private String Content_Type ="text/html";
}
