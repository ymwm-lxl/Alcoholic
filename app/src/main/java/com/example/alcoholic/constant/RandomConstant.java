package com.example.alcoholic.constant;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by
 * Description: 随机常量类
 * on 2020/11/17.
 */
public class RandomConstant {


    /**
     * 随机生成头像
     * @return
     */
    public static String randomHeadImg(){
        String[] heads = new String[]{
                "https://i.ibb.co/7X1LDkv/de-head-1.jpg",
                "https://i.ibb.co/VHHn11n/de-head-2.jpg"
        };
        int index = ThreadLocalRandom.current().nextInt(0, (heads.length-1));
        return heads[index];
    }

}
