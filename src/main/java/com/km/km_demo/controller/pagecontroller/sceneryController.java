package com.km.km_demo.controller.pagecontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.km.km_demo.dao.dto.indexMainInfoDTO;
import com.km.km_demo.dao.dto.sceneryDiscationDTO;
import com.km.km_demo.dao.entity.*;
import com.km.km_demo.matchsys.matchSystem;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.service.*;
import com.km.km_demo.util.NetConnectUtils;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.myResult;
import com.km.km_demo.util.timeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br>
 * 〈景点主页〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/10 0010
 * @since 1.0.0
 */
@RestController
@CrossOrigin
@RequestMapping("/scenery")
@EnableAutoConfiguration
public class sceneryController {

    @Autowired
    userService myUserService;

    @Autowired
    sceneryService mySceneryService;

    @Autowired
    commentService myCommentService;

    @Autowired
    replyService myReplyService;

    @Autowired
    RedisServiceImpl myRedisService;

    @Autowired
    bookService myBookService;

    @Autowired
    commonUtil cu;

    @Autowired
    matchSystem ms;

    //设置标志位
    int isok=0;

    //发表评论
    @RequestMapping("/commentscenery")
    public myResult commentScenery(@RequestBody JSONObject Ocomment){

        myResult NMR=new myResult();
        try {
            comment Ncomment = Ocomment.toJavaObject(comment.class);
            myCommentService.save(Ncomment);
            NMR.setStatecode(1);
            NMR.setMessage("发表成功");
        } catch (Exception e) {
            e.printStackTrace();
            NMR.setStatecode(0);
            NMR.setMessage("发表失败，请联系管理员");
        }
        return NMR;
    }


    @RequestMapping("/getdiscation/page")
    public myResult getdiscationpage(@RequestParam(value="sceneryid")Integer sceneryid,
                                     @RequestParam(value="pagesize")int pagesize,
                                     @RequestParam(value="pagecurrent")int pagecurrent){
        //先初始化返回数据
        List<comment> commentList = new LinkedList<comment>();
        Map<String,List<reply>> replyListMap=new HashMap<String,List<reply>>();

        System.out.println("分页查询");

        myResult NmyResult=new myResult();
        try {
            //System.out.println(location);
            //通过前台传过来的景点分页数据，去数据库查询对应区域的评论和回复数据。
            IPage<comment> commentPage=new Page<comment>(pagecurrent,pagesize);
            IPage<comment> resultpage = myCommentService.page(commentPage,new QueryWrapper<comment>().eq("sceneryid",sceneryid));

            resultpage.setSize(pagesize);
            resultpage.setCurrent(pagecurrent);
            //将分页数据转化为list
            commentList=resultpage.getRecords();
            System.out.println(commentList);

            //通过查到的评论数据，查询景点对应的回复。只显示一条
            List<reply> replyList=new LinkedList<reply>();
            //默认只查询一条评论出来
            replyListMap=new HashMap<>();

            for (comment Ocomment : commentList
            ) {
                replyList=myReplyService.list(new QueryWrapper<reply>()
                        .eq("replycommentid",Ocomment.getCommentid())
                        .like("replytype","comment")
                        .last("limit 1"));
                replyListMap.put(Ocomment.getSceneryid()+"",replyList);
            }
            //System.out.println(commentListMap);
            //组装进dto类
            sceneryDiscationDTO NsceneryDiscationDTO= new sceneryDiscationDTO(commentList,replyListMap);

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(NsceneryDiscationDTO);
        }catch (Exception e){
            //如果查询出错的话
            e.printStackTrace();
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，暂无相关信息");
            NmyResult.setContent(null);
        }
        return NmyResult;
    }

    //查看景点下的讨论
    @RequestMapping("/getdiscation")
    public myResult getdiscation(@RequestParam(value="sceneryid")Integer sceneryid){

        //默认的返回数据
        myResult NMyResult=new myResult(0,"对不起，由于未知错误无法查询，请联系管理员","");
        //try catch防止系统奔溃
        try {
            //获取景点下的评论
            List<comment> commentList = myCommentService.list(new QueryWrapper<comment>().eq("sceneryid",sceneryid));
            //获取评论下的回复
            List<reply> replyList=null;
            Map<String,List<reply>> replyListMap=new HashMap<>();
            for (comment Ocomment : commentList
            ) {
                replyList=myReplyService.list(new QueryWrapper<reply>()
                        .eq("replycommentid",Ocomment.getCommentid())
                .like("replytype","comment"));
                replyListMap.put(Ocomment.getCommentid()+"",replyList);
            }
            //修改返回值



        }catch (Exception e){
            e.printStackTrace();

        }

        //返回list给前台
        return NMyResult;
    }

    //预约景点
    // 从前台获取景点和用户的id
    @RequestMapping("/bookScenery")
    public myResult bookScenery(@RequestParam(value="sceneryid")Integer sceneryid,@RequestParam(value="booktime")String booktime,@RequestParam(value="userid")Integer userid){

        // 定义返回数据，假设匹配失败
        myResult NMyResult=new myResult(0,"对不起，由于未知错误无法匹配，请联系管理员","");
        //预约流程：先查询缓存锁，如果为悲伤所
        //使用定长线程池进行操作
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

        fixedThreadPool.execute(
                new Runnable() {
                    //通过线程执行匹配操作
                    @Override
                    public void run() {
                        //放在try中
                        try {
                            //先查询redis匹配队列，查找之前总是判断锁，
                            while(cu.checklock("redismatchlock")==0){
                                System.out.println("等待锁中");
                            }
                            // 获取锁以后先给其加锁。
                            // 查找的时候上悲观锁（修改监视字段为0）
                            //悲观锁：redis里设置一个值作为监视字段，该字段为1时可以查，为0时不可查
                            myRedisService.set("redismatchlock","0");
                            //如果不存在匹配队列，设置匹配队列
                            if(myRedisService.get("matchQueue")==null){
                                List<book> bookList=new LinkedList<book>();
                                myRedisService.set("matchQueue",bookList);
                            }
                            // 获取匹配队列
                            List<book> bookList = (List<book>)myRedisService.get("matchQueue");
                            // 将前台得到的信息添加到匹配队列
                            book Nbook=new book();
                            Nbook.setBooksceneryid(sceneryid);
                            Nbook.setGenerattime(new timeUtil().getNowTime());
                            Nbook.setBooktime(booktime);
                            Nbook.setUserid(userid);
                            // 修改其预定表状态为匹配中
                            Nbook.setBookstate("matching");
                            // 将预约信息写入数据库
                            myBookService.save(Nbook);
                            // 加入匹配队列
                            bookList.add(Nbook);

                            // 将新的匹配队列写入缓存
                            myRedisService.set("matchQueue",bookList);
                            // 通知匹配系统进行匹配
                            ms.ms("matchQueue");
                            // 释放锁
                            myRedisService.set("redismatchlock","1");
                            // 修改返回信息
                            NMyResult.setStatecode(1);
                            NMyResult.setMessage("匹配中");
                        } catch (Exception e) {
                            e.printStackTrace();

                        }finally {
                            isok=1;
                        }
                    }
                }
        );
        while (isok==0){}
        isok=0;
        return NMyResult;
    }

    //通过replycommentid，查询对应的所有回复
    @RequestMapping(value = "/getgeply")
    public myResult getgeplybyreplycommentid(@RequestParam("replycommentid")int replycommentid){

        myResult NMR=new myResult();
        try {
            reply Nreply = myReplyService.getOne(new QueryWrapper<reply>().eq("replycommentid",replycommentid));
            NMR.setStatecode(1);
            NMR.setMessage("ok");
            NMR.setContent(Nreply);
        } catch (Exception e) {
            e.printStackTrace();
            NMR.setStatecode(0);
            NMR.setMessage("查询失败，请联系管理员");
        }
        return NMR;
    }




}
