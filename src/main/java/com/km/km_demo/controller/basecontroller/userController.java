package com.km.km_demo.controller.basecontroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.km.km_demo.dao.entity.user;
import com.km.km_demo.service.userService;
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
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2019/11/16 0016
 * @since 1.0.0
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin")
@CrossOrigin
public class userController {

    @Autowired
    userService userService;

    //管理员界面的增删查改

    //统一定义返回值
    myResult MR;

    @RequestMapping("/add")
    public myResult add(@RequestParam(value="user") user Ouser){



        //根据前台传过来的user添加
        try {

            //如果操作成功
            if(userService.save(Ouser)){
                // 修改返回值
                MR.setStatecode(1);
                MR.setMessage("ok");
            }
            else {
                MR.setStatecode(0);
                MR.setMessage("未知错误，请查看日志");
            }
        }catch (Exception e){
            e.printStackTrace();
            MR.setStatecode(0);
            MR.setMessage("未知错误，请查看日志");
        }
        return MR;
    }

    @RequestMapping(value = "/update")
    public myResult update_id(@RequestParam(value="user")user Ouser,@RequestParam(value="userid")int userid){
        try {

            Ouser.setUserid(userid);
            //如果修改成功
            if(userService.update(new UpdateWrapper<user>(Ouser))){
                // 修改返回值
                MR.setStatecode(1);
                MR.setMessage("ok");
            }
            else {
                MR.setStatecode(0);
                MR.setMessage("未知错误，请查看日志");
            }
        }catch (Exception e){
            e.printStackTrace();
            MR.setStatecode(0);
            MR.setMessage("未知错误，请查看日志");
        }
        return MR;
    }
/*
    @RequestMapping("/delete")
    public myResult delete_id(@RequestParam(value="id")int id){


        //组装返回值
        myResult cr=new myResult(null,"ok",1);
        //根据前台传过来的id删除对应user
        try {
            userService.removeById(id);
        }catch (Exception e){
            //组装返回值
            cr=new myResult(null,"nook",1);
            e.printStackTrace();

        }

        return cr;
    }

    @RequestMapping("/select/query")
    public myResult select_query(@RequestParam(value="username",defaultValue="0")String username,@RequestParam(value="status")Integer status){

        //条件构造器
        QueryWrapper<User> qwu=new QueryWrapper<>();
        //如果没有勾选status
        if(status==null)
        {

            qwu.like("username","username");
            //通过条件构造器查询得到返回列表
            List<User> lu=userService.list(qwu);
            //输出验证
            System.out.println(lu.get(0).toString());
            //组装返回对象
            //对象，返回信息，状态码
            myResult cr=new myResult(lu,"ok",1);

            return cr;
        }
        //勾选了状态则添加状态的查询
        qwu.like("username","username").eq("status",status);
        //通过条件构造器查询得到返回列表
        List<User> lu=userService.list(qwu);
        //输出验证
        System.out.println(lu.get(0).toString());
        //组装返回对象
        //对象，返回信息，状态码
        myResult cr=new myResult(lu,"ok",1);

        return cr;
    }

    @RequestMapping("/select/id")
    //value值对应前台的字段
    public myResult select_id(@RequestParam(value="id",defaultValue="0")int id){


        //通过id查询得到返回对象
        User user=userService.getById(id);
        //输出验证
        System.out.println(user.toString());
        //组装返回对象
        //对象，返回信息，状态码
        myResult cr=new myResult(user,"ok",1);

        return cr;
    }

    @RequestMapping("/select/all")
    public myResult select_all(){

        //条件构造器
        QueryWrapper<User> qwu=new QueryWrapper<>();
        //通过条件构造器查询得到返回列表
        List<User> lu=userService.list(qwu);
        //输出验证
        System.out.println(lu.get(0).toString());
        //组装返回对象
        //对象，返回信息，状态码
        myResult cr=new myResult(lu,"ok",1);

        return cr;
    }

    @RequestMapping("/select/page")
    public myResult select_page(@RequestParam(value="page",defaultValue="1")int page,
                                 @RequestParam(value="limit",defaultValue="10")int size){

        //条件构造器
        QueryWrapper<User> qwu=new QueryWrapper<>();

        //通过条件构造器查询得到返回列表
        List<User> lu=userService.list(qwu);

        //分页
        List<User> rlu=lu.subList((page-1)*size,(page-1)*size+size+1);
        //输出验证
        //System.out.println(rlu.get(0).toString());
        //组装返回对象
        //对象，返回信息，状态码
        myResult cr=new myResult(rlu,"ok",1);

        return cr;
    }

    @RequestMapping("/test")
    public List<User> test(){

        //条件构造器
        QueryWrapper<User> qwu=new QueryWrapper<>();
        //通过条件构造器查询得到返回列表
        List<User> lu=userService.list(qwu);

        lu.get(0).setId(0);

        //根据前台传过来的user添加
        try {
            userService.save(lu.get(0));
        }catch (Exception e){
            e.printStackTrace();
        }

        return lu;
    }

 */
}
