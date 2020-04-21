package com.km.km_demo.controller.pagecontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.km.km_demo.dao.entity.comment;
import com.km.km_demo.dao.entity.scenery;
import com.km.km_demo.dao.entity.user;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.middleware.redis.redis;
import com.km.km_demo.service.*;
import com.km.km_demo.util.NetConnectUtils;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.myResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈专门用来测试的控制器〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/12 0012
 * @since 1.0.0
 */
@RequestMapping("/test")
@RestController
@CrossOrigin
@EnableAutoConfiguration
public class testController {

    @Autowired
    userService myUserService;

    @Autowired
    sceneryService mySceneryService;

    @Autowired
    commentService myCommentService;

    @Autowired
    replyService myReplyService;

    @Autowired
    bookService myBookService;

    @Autowired
    orderService myOrderService;

    @Autowired
    letterService myLetterService;

    @Autowired
    RedisServiceImpl myRedisService;

    @Autowired
    commonUtil cu;

    String useraccountname="uat";
    String userpassword="upw";
    String email="@qq.com";
    int tel=70000;
    int qq=10000;
    int num=1;
    user vuser=new user();

    //将爬虫搜集到的数据整理到数据库中
    @RequestMapping("/updateuserDB")
    public String updateuserDB(){

        //取出景点评论数据
        List<comment> commentList=myCommentService.list();
        //对于每一条评论数据，将其评论者取出
        commentList.forEach(commentx -> {
            //自动生成其账号，密码，电话，qq，邮箱
            //组装成user类
            vuser.setUserannoyname(commentx.getUserannoyname());
            vuser.setUseraccountname(useraccountname+num);
            vuser.setUserpassword(userpassword+num);
            vuser.setEmail(num+email);
            vuser.setUserqq(qq+num);
            vuser.setUsertel(tel+num);
            num++;
            //写入user表
            myUserService.save(vuser);

            //再从user表中取出其id，写入comment表
            try {
                commentx.setUserid(myUserService.getOne(new QueryWrapper<user>()
                        .eq("useraccountname",vuser.getUseraccountname())).getUserid());
                myCommentService.update(new UpdateWrapper<comment>()
                        .eq("commentid",commentx.getCommentid())
                        .set("userid",commentx.getUserid()));
            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "ok";
    }

    //将爬虫搜集到的数据整理到数据库中
    @RequestMapping("/updateDB")
    public String updateDB(){

        //取出景点数据
        List<scenery> sceneryList=mySceneryService.list();
        //取出景点评论数据
        List<comment> commentList=myCommentService.list();

        //将景点评论数据中的sceneryid字段，修改成对应的景点id
        sceneryList.forEach(sceneryx -> {
            myCommentService.update(new UpdateWrapper<comment>()
                    .like("sceneryname",sceneryx.getSceneryname())
                    .set("sceneryid",sceneryx.getSceneryid()));
        });

        return "ok";
    }

    @RequestMapping("/android")
    public String testAndroid(){

        return "安卓调用成功";
    }

    @RequestMapping("/hucget1")//@RequestParam("info")String info
    public myResult testhucget(){
        myResult NMR=null;
        //JSONObject jsonObject=null;
        user xuser=new user();
        xuser.setUserqq(123456);
        xuser.setEmail("asdasda");

        String jsonstring = JSONObject.toJSONString(xuser);
        try {
            NMR=new NetConnectUtils().urlPost("/test/hucpost",jsonstring);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return NMR;
    }

    @RequestMapping("/hucget2")//
    public myResult testhucget2(@RequestParam(value ="info")String info){

        myResult NMR=new myResult();

        NMR.setContent(info);
        return NMR;
    }

    @RequestMapping("/hucpost")
    public myResult testhucpost(@RequestBody JSONObject jsonParam){
        myResult NMR=new myResult();
        try {
            user Ouser = jsonParam.toJavaObject(user.class);
            NMR=new NetConnectUtils().urlGet("/test/android");
            System.out.println(Ouser);
            NMR.setContent(Ouser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NMR;
    }

    @RequestMapping("/resetredislock1")
    public String testhucpost(){
        myRedisService.set("redismatchlock",1);
        return "ok";
    }
/**
    @RequestMapping("/redistest1")
    public String redistest1(){
        myRedisService.set("cjk","cjv");
        myRedisService.get("cjk");
        cu.getverifyCode("","");
        return myRedisService.get("cjk").toString();
    }

    @RequestMapping("/redistest2")
    public String redistest2(){
        myRedisService.set("cjk2","cjv2");
        myRedisService.get("cjk");
        getverifyCode("123","123");

        return myRedisService.get("cjk").toString();
    }
    **/


}
