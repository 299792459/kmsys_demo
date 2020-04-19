package com.km.km_demo.controller.pagecontroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.km.km_demo.dao.entity.book;
import com.km.km_demo.dao.entity.comment;
import com.km.km_demo.dao.entity.reply;
import com.km.km_demo.dao.entity.scenery;
import com.km.km_demo.matchsys.matchSystem;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.service.*;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.myResult;
import com.km.km_demo.util.timeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    //发表评论

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
                replyList=myReplyService.list(new QueryWrapper<reply>().eq("replycommentid",Ocomment.getCommentid()));
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
                                wait(1);
                            }
                            // 获取锁以后先给其加锁。
                            // 查找的时候上悲观锁（修改监视字段为0）
                            //悲观锁：redis里设置一个值作为监视字段，该字段为1时可以查，为0时不可查
                            myRedisService.set("redismatchlock","0");
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
                            new matchSystem().ms("matchQueue");
                            // 释放锁
                            myRedisService.set("redismatchlock","1");
                            // 修改返回信息
                            NMyResult.setStatecode(1);
                            NMyResult.setMessage("匹配中");
                        } catch (Exception e) {
                            e.printStackTrace();;

                        }
                    }
                }
        );
        return NMyResult;
    }


}
