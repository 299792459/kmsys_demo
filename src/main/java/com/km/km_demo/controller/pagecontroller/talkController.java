package com.km.km_demo.controller.pagecontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.km.km_demo.dao.entity.*;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.service.*;
import com.km.km_demo.util.NetConnectUtils;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.myResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
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
@RequestMapping("/talk")
@RestController
@CrossOrigin
@EnableAutoConfiguration
public class talkController {

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


    @RequestMapping("/android")
    public String testAndroid(){

        return "安卓调用成功";
    }

    //发表回复
    @RequestMapping("/reply")
    public myResult replycomment(@RequestBody JSONObject replyinfo){

        myResult NMR=new myResult();
        try {
            reply Nreply = replyinfo.toJavaObject(reply.class);
            myReplyService.save(Nreply);
            NMR.setStatecode(1);
            NMR.setMessage("发表成功");
        } catch (Exception e) {
            e.printStackTrace();
            NMR.setStatecode(0);
            NMR.setMessage("发表失败，请联系管理员");
        }
        return NMR;
    }


    @RequestMapping("/comment")
    public myResult listcomment(@RequestParam(value = "commentid") int commentid){

        myResult NMR=new myResult();
        List<reply> replyList=new LinkedList<reply>();
        try {
            replyList = myReplyService.list(new QueryWrapper<reply>()
                    .eq("replycommentid",commentid)
                    .like("replytype","comment"));
            NMR.setContent(replyList);
            NMR.setStatecode(1);
            NMR.setMessage("OK");
        }catch (Exception e){
            NMR.setStatecode(1);
            NMR.setMessage(e.getMessage());
        }
        return NMR;
    }

    @RequestMapping("/letter")
    public myResult listletter(@RequestParam(value = "letterid") int letterid){

        myResult NMR=new myResult();
        List<reply> replyList=new LinkedList<reply>();
        try {
            replyList = myReplyService.list(new QueryWrapper<reply>()
                    .eq("replycommentid",letterid)
                    .like("replytype","letter"));
            NMR.setContent(replyList);
            NMR.setStatecode(1);
            NMR.setMessage("OK");
        }catch (Exception e){
            NMR.setStatecode(1);
            NMR.setMessage(e.getMessage());
        }
        return NMR;
    }

}
