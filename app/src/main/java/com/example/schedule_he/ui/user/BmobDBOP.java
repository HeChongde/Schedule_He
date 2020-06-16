package com.example.schedule_he.ui.user;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BmobDBOP {
    //添加单条数据
    public void insert(User ob){
        ob.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    //添加成功
                    //LogUtil.e(RegisterActivity.class,"========objectId="+objectId);
                    Log.d("he", "插入正常"+e.getMessage());
                }else{
                    //添加失败
                    //LogUtil.e(RegisterActivity.class,"========e="+e.getErrorCode()+"   errorMessage="+e.getMessage());
                    Log.d("he", "插入异常"+e.getMessage()+"错误其他信息"+e.getErrorCode());
                }
            }
        });
    }


    //更新单条数据
    public void update(BmobObject ob, String objectId){//objectid是bmob后台自动生成的唯一id
        ob.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //LogUtil.e(MainActivity.class,"===更新成功===");
                }else{
                   // LogUtil.e(MainActivity.class,"更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    /**删除整条数据**/
    public void deleteObject(BmobObject ob, String objectId){
        ob.setObjectId(objectId);
        ob.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                   // LogUtil.e(MainActivity.class,"===删除成功===");
                }else{
                   // LogUtil.e(MainActivity.class,"删除失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**单个查询  用户信息**/
    public void checkByObjectId(String objectId){
        new BmobQuery<User>().getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                    //LogUtil.e(MainActivity.class,"======person  getNickName="+person.getNickName()+"  getObjectId="+person.getObjectId()+"   getCreatedAt="+person.getCreatedAt());
                }else{
                    //LogUtil.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private List<User> ob = new ArrayList<>();
    /**单个查询  用户信息**/
    public List<User> checkByName(String uname) {

        BmobQuery<User> query = new BmobQuery<User>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", uname);
        //执行查询方法
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    ob=object;
                    //toast("查询成功：共" + object.size() + "条数据。");
                    Log.d("he", "查询成功   共有"+object.size()+"条数据"+ob.size() );
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        Log.d("he", "查aaaaaaaaaa啊啊啊啊嗄啊啊啊啊"+ob.size()+"条数据"+ob.size() );
        return ob;
    }
}
