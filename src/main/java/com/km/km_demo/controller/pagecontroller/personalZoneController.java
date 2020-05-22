package com.km.km_demo.controller.pagecontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.km.km_demo.dao.dto.letterDiscationDTO;
import com.km.km_demo.dao.dto.sceneryDiscationDTO;
import com.km.km_demo.dao.entity.*;
import com.km.km_demo.service.*;
import com.km.km_demo.util.myResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    //发表留言
    @RequestMapping("/letteruser")
    public myResult commentScenery(@RequestBody JSONObject letteruserinfo){

        myResult NMR=new myResult();
        try {
            letter Nletter = letteruserinfo.toJavaObject(letter.class);
            myLetterService.save(Nletter);
            NMR.setStatecode(1);
            NMR.setMessage("发表成功");
        } catch (Exception e) {
            e.printStackTrace();
            NMR.setStatecode(0);
            NMR.setMessage("发表失败，请联系管理员");
        }
        return NMR;
    }

    @RequestMapping("/update/userinfo")
    public myResult updateuserinfo(@RequestBody JSONObject userinfo){

        myResult NMR=new myResult();
        try {
            user Nuser = userinfo.toJavaObject(user.class);
            myUserService.update(new UpdateWrapper<user>()
                    .eq("userid",Nuser.getUserid())
                    .setEntity(Nuser));

            NMR.setStatecode(1);
            NMR.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            NMR.setStatecode(0);
            NMR.setMessage("修改失败，请联系管理员");
        }
        return NMR;
    }

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


    @RequestMapping("/getRevicedLetters/page")
    public myResult getdiscationpage(@RequestParam(value="letteruserid")Integer letteruserid,
                                     @RequestParam(value="pagesize")int pagesize,
                                     @RequestParam(value="pagecurrent")int pagecurrent){
        //先初始化返回数据
        List<letter> letterList = new LinkedList<letter>();
        Map<String,List<reply>> replyListMap=new HashMap<String,List<reply>>();

        System.out.println("分页查询");

        myResult NmyResult=new myResult();
        try {
            //System.out.println(location);
            //通过前台传过来的景点分页数据，去数据库查询对应区域的评论和回复数据。
            IPage<letter> commentPage=new Page<letter>(pagecurrent,pagesize);
            IPage<letter> resultpage = myLetterService.page(commentPage,new QueryWrapper<letter>().eq("letteruserid",letteruserid));

            resultpage.setSize(pagesize);
            resultpage.setCurrent(pagecurrent);
            //将分页数据转化为list
            letterList=resultpage.getRecords();
            System.out.println(letterList);

            //通过查到的评论数据，查询景点对应的回复。只显示一条
            List<reply> replyList=new LinkedList<reply>();
            //默认只查询一条评论出来
            replyListMap=new HashMap<>();

            for (letter Oletter : letterList
            ) {
                replyList=myReplyService.list(new QueryWrapper<reply>()
                        .eq("replyuserid",Oletter.getUserid())
                        .like("replytype","letter")
                        .last("limit 1"));
                replyListMap.put(Oletter.getLetterid()+"",replyList);
            }
            //System.out.println(commentListMap);
            //组装进dto类
            letterDiscationDTO NletterDiscationDTO= new letterDiscationDTO(letterList,replyListMap);

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(NletterDiscationDTO);
        }catch (Exception e){
            //如果查询出错的话
            e.printStackTrace();
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，暂无相关评论");
            NmyResult.setContent(null);
        }
        return NmyResult;
    }

    /*
    //用户可通过邮箱验证更改忘记的密码（这个不放在登录界面中）
    @RequestMapping("/updatePwdByEmail")
    public myResult updatePwdByEmail(@RequestParam(value="email")String Email){
        return new myResult(1,"您已成功修改密码，请牢记",null);
    }*/

    //获取个人的基本信息
    //输入用户信息
    @RequestMapping("/getUserInfo")
    public myResult getUserInfo(@RequestParam(value = "userid")String userid) {
        myResult MR=new myResult();
        user Nuser = new user();
        try {
            Nuser = myUserService.getById(userid);
            MR=new myResult(1,"ok",Nuser);
        } catch (Exception e) {
            e.printStackTrace();
            MR=new myResult(1,"失败，请联系管理员",null);
        }
        return MR;
    }

    //获取个人的基本信息
    //输入用户信息
    @RequestMapping("/getUserNameById")
    public myResult getUserNameById(@RequestParam(value = "userid")String userid) {
        myResult MR=new myResult();
        user Nuser = new user();
        try {
            Nuser = myUserService.getOne(new QueryWrapper<user>().eq("userid",userid));
            MR=new myResult(1,"ok",Nuser.getUserannoyname());
        } catch (Exception e) {
            e.printStackTrace();
            MR=new myResult(1,"失败，请联系管理员",null);
        }
        return MR;
    }




}
