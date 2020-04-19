package com.km.km_demo.controller.pagecontroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.km.km_demo.dao.dto.indexMainInfoDTO;
import com.km.km_demo.dao.entity.comment;
import com.km.km_demo.dao.entity.scenery;
import com.km.km_demo.dao.entity.user;
import com.km.km_demo.service.commentService;
import com.km.km_demo.service.impl.userServiceImpl;
import com.km.km_demo.service.replyService;
import com.km.km_demo.service.sceneryService;
import com.km.km_demo.service.userService;
import com.km.km_demo.util.myResult;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈主页〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/5 0005
 * @since 1.0.0
 */

@RestController
@CrossOrigin
@RequestMapping("/index")
@EnableAutoConfiguration
public class indexController {

    @Autowired
    userService myUserService;

    @Autowired
    sceneryService mySceneryService;

    @Autowired
    commentService myCommentService;

    @Autowired
    replyService myReplyService;



    @RequestMapping("/getMainInfo/page")
    public myResult getMainInfoByPage(@RequestParam(value="location")String location,@RequestParam(value="pagesize")int pagesize,@RequestParam(value="pagecurrent")int pagecurrent){

        //先初始化返回数据
        List<scenery> sceneryList = new LinkedList<scenery>();
        Map<String,List<comment>> commentListMap=new HashMap<String,List<comment>>();

        System.out.println("分页查询");

        myResult NmyResult=new myResult();
        try {
            //System.out.println(location);
            //通过前台传过来的地址和分页数据，去数据库查询对应区域的景点数据。
            IPage<scenery> sceneryPage=new Page<scenery>(pagecurrent,pagesize);
            IPage<scenery> resultpage = mySceneryService.page(sceneryPage,new QueryWrapper<scenery>().like("location",location));
            System.out.println(sceneryList);
            resultpage.setSize(pagesize);
            resultpage.setCurrent(pagecurrent);
            //将分页数据转化为list
            sceneryList=resultpage.getRecords();

            System.out.println(sceneryList);


            //System.out.println(sceneryList);
            //通过查到的景点数据，查询景点对应的评论。只显示一条
            List<comment> commentList=new LinkedList<comment>();

            //默认只查询一条评论出来
            commentListMap=new HashMap<>();
            for (scenery Oscenery : sceneryList
            ) {
                commentList=myCommentService.list(new QueryWrapper<comment>()
                        .eq("sceneryid",Oscenery.getSceneryid())
                        .last("limit 1"));
                commentListMap.put(Oscenery.getSceneryid()+"",commentList);
            }
            //System.out.println(commentListMap);
            //组装进dto类
            indexMainInfoDTO NindexMainInfoDTO= new indexMainInfoDTO(sceneryList,commentListMap);

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(NindexMainInfoDTO);
        }catch (Exception e){

            //如果查询出错的话
            e.printStackTrace();
            //组装进dto类
            //indexMainInfoDTO NindexMainInfoDTO= new indexMainInfoDTO(sceneryList,commentListMap);
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，查询出错，请联系管理员");
            NmyResult.setContent(null);
        }



        return NmyResult;
    }

    @RequestMapping("/getMainInfo")
    public myResult getMainInfo(@RequestParam(value="location")String location){

        //先初始化返回数据
        List<scenery> sceneryList = new LinkedList<scenery>();
        Map<String,List<comment>> commentListMap=new HashMap<String,List<comment>>();
        myResult NmyResult=new myResult();
        try {
            //System.out.println(location);
            //通过前台传过来的地址，去数据库查询对应区域的景点数据。
            sceneryList=mySceneryService.list(new QueryWrapper<scenery>().like("location",location));

            //System.out.println(sceneryList);
            //通过查到的景点数据，查询景点对应的评论。
            List<comment> commentList=new LinkedList<comment>();
            commentListMap=new HashMap<>();
            for (scenery Oscenery : sceneryList
            ) {
                commentList=myCommentService.list(new QueryWrapper<comment>().eq("sceneryid",Oscenery.getSceneryid()));
                commentListMap.put(Oscenery.getSceneryid()+"",commentList);
            }
            //System.out.println(commentListMap);
            //组装进dto类
            indexMainInfoDTO NindexMainInfoDTO= new indexMainInfoDTO(sceneryList,commentListMap);

            NmyResult.setStatecode(1);
            NmyResult.setMessage("ok");
            NmyResult.setContent(NindexMainInfoDTO);
        }catch (Exception e){

            //如果查询出错的话
            e.printStackTrace();
            //组装进dto类
            indexMainInfoDTO NindexMainInfoDTO= new indexMainInfoDTO(sceneryList,commentListMap);
            NmyResult.setStatecode(0);
            NmyResult.setMessage("对不起，查询出错，请联系管理员");
            NmyResult.setContent(null);
        }



        return NmyResult;
    }


}
