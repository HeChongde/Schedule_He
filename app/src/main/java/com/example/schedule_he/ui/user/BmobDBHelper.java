package com.example.schedule_he.ui.user;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Title:Bmob数据存储帮助类
 * Description:
 * <p>
 * Created by pei
 * Date: 2018/2/27
 */
public class BmobDBHelper {

    //这里APPLICATION_ID写你自己应用的ApplicationID
    private static final String APPLICATION_ID="95b8555d2593e62f1c3d64c19785f67d";


    private BmobDBHelper() {
    }

    private static class Holder {
        private static BmobDBHelper instance = new BmobDBHelper();
    }

    public static BmobDBHelper getInstance() {
        return Holder.instance;
    }

    /**初始化**/
    public void init(Context context){
        //自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(context)
                .setApplicationId(APPLICATION_ID)//设置appkey
//                .setConnectTimeout(30)//请求超时时间（单位为秒）：默认15s
//                .setUploadBlockSize(1024*1024)//文件分片上传时每片的大小（单位字节），默认512*1024
//                .setFileExpiration(2500)//文件的过期时间(单位为秒)：默认1800s
                .build();
        Bmob.initialize(config);
    }

}
