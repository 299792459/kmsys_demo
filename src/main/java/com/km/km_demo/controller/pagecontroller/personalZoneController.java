package com.km.km_demo.controller.pagecontroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.km.km_demo.dao.entity.book;
import com.km.km_demo.dao.entity.comment;
import com.km.km_demo.dao.entity.letter;
import com.km.km_demo.dao.entity.order;
import com.km.km_demo.service.*;
import com.km.km_demo.util.myResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈个人主页〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/7 0007
 * @since 1.0.0
 */
@RequestMapping("/pz")
@RestController
@CrossOrigin
@EnableAutoConfiguration
public class personalZoneController {

    @Autowired
    userService myUserService;

    @Autowired
    sceneryService mySceneryService;

    @Autowired
    commentService myCommentService;

    @Autowired
    replyService myReplyService;

    @Autowired
    letterService myLetterService;

    @Autowired
    orderService myOrderService;

    @Autowired
    bookService myBookService;


    // 个人主页主要功能

    // 查看自己发过的景点评论
    @RequestMapping("/getMySceneryComments")
    public myResult getMySceneryComments(@RequestParam(value="userid")String userid,@RequestParam(value="sceneryid")String sceneryid){

        myResult NmyResult=new myResult();
        try {
            List<comment> commentList = myCommentService.list(new QueryWrapper<comment>().eq("userid",userid).eq("sceneryid",sceneryid));

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(commentList);
            return NmyResult;
        }catch (Exception e){
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，由于未知错误未能查询到数据，请联系管理员");
            return NmyResult;
        }
    }


    // 查看自己给别人的留言
    @RequestMapping("/getMyLetters")
    public myResult getMyLetters(@RequestParam(value="userid")String userid){

        myResult NmyResult=new myResult();
        try {
            List<letter> letterList = myLetterService.list(new QueryWrapper<letter>().eq("userid",userid));

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(letterList);
            return NmyResult;
        }catch (Exception e){
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，由于未知错误未能查询到数据，请联系管理员");
            return NmyResult;
        }
    }

    //查看自己完成的订单，去过的地方
    @RequestMapping("/getMyOrders")
    public myResult getMyOrders(@RequestParam(value="userid")String userid){


        myResult NmyResult=new myResult();
        try {
            List<order> orderList = myOrderService.list(new QueryWrapper<order>().eq("userid",userid));

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(orderList);
            return NmyResult;
        }catch (Exception e){
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，由于未知错误未能查询到数据，请联系管理员");
            return NmyResult;
        }
    }
    //查看自己的预约
    @RequestMapping("/getmybooks")
    public myResult getmybooks(@RequestParam(value="userid")String userid){


        myResult NmyResult=new myResult();
        try {
            List<book> bookList = myBookService.list(new QueryWrapper<book>().eq("userid",userid));

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(bookList);
            return NmyResult;
        }catch (Exception e){
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，由于未知错误未能查询到数据，请联系管理员");
            return NmyResult;
        }
    }

    // 查看别人给自己的留言（空间留言板）
    @RequestMapping("/getRevicedLetters")
    public myResult getRevicedComments(@RequestParam(value="letteruserid")String letteruserid){

        myResult NmyResult=new myResult();
        try {
            List<letter> letterList = myLetterService.list(new QueryWrapper<letter>().eq("letteruserid",letteruserid));
            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(letterList);
            return NmyResult;
        }catch (Exception e){
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，由于未知错误未能查询到数据，请联系管理员");
            return NmyResult;
        }
    }

    /*
    //用户可通过邮箱验证更改忘记的密码（这个不放在登录界面中）
    @RequestMapping("/updatePwdByEmail")
    public myResult updatePwdByEmail(@RequestParam(value="email")String Email){
        return new myResult(1,"您已成功修改密码，请牢记",null);
    }*/




}
