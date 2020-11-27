package com.example.alcoholic.constant;

import com.orhanobut.logger.Logger;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by
 * Description: 随机常量类
 * on 2020/11/17.
 */
public class RandomConstant {


    /**
     * 随机生成头像
     */
    public static String randomHeadImg(){
        String[] heads = new String[]{
                "https://i.ibb.co/7X1LDkv/de-head-1.jpg",
                "https://i.ibb.co/VHHn11n/de-head-2.jpg",
                "https://i.ibb.co/RP4RPFJ/de-head-3.jpg",
                "https://i.ibb.co/n02XkJh/de-head-4.jpg",
                "https://i.ibb.co/sK43432/de-head-5.jpg",
                "https://i.ibb.co/B4SfH3D/de-head-6.jpg",
                "https://i.ibb.co/grYt2bK/de-head-7.jpg",
                "https://i.ibb.co/n0HZsWp/de-head-8.jpg",
                "https://i.ibb.co/y5t1KBv/de-head-9.jpg",
                "https://i.ibb.co/8zw2NyW/de-head-10.jpg",
                "https://i.ibb.co/6sTVSrn/de-head-11.jpg",
        };
        int index = ThreadLocalRandom.current().nextInt(0, (heads.length-1));
        return heads[index];
    }

}
