package com.km.km_demo.controller.pagecontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.km.km_demo.dao.entity.user;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.service.userService;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.javaMailUtil;
import com.km.km_demo.util.myResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈注册页面〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/5 0005
 * @since 1.0.0
 */
@RestController
@CrossOrigin
@RequestMapping("/regist")
@EnableAutoConfiguration
public class registController {

    @Autowired
    userService myUserService;

    @Autowired
    javaMailUtil myjavaMailUtil;

    @Autowired
    RedisServiceImpl myRedis;

    @Autowired
    commonUtil cu;


    //用户先验证邮箱
    @RequestMapping("/email")
    public myResult verifyEmail(@RequestParam(value="Email")String Email){
        return myjavaMailUtil.getVCByEmail(Email);
    }

    @RequestMapping("/verifycode")
    public myResult verifyCode(@RequestParam(value="Email")String Email,@RequestParam(value="verifycode")String verifycode){
        //System.out.println(Email+"   "+verifycode);
        myResult mr = cu.getverifyCode(verifycode,Email);
        return mr;
    }
    //输入用户信息
    @RequestMapping("/userInfo")
    public myResult setUserInfo(@RequestBody JSONObject jsonuser){
        user Ouser=new user();
        try {
            Ouser = jsonuser.toJavaObject(user.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        //验证用户名是否存在
        if(myUserService.getOne(new QueryWrapper<user>().eq("useraccountname",Ouser.getUseraccountname()))==null){
            //保存用户信息
            try {
                myUserService.save(Ouser);

                return new myResult(1,"注册成功","");
            }catch (Exception e){
                e.printStackTrace();
                return new myResult(0,"未知错误注册失败，请联系管理员","");
            }
        }else{
            //已经存在的话则返回已存在并建议修改
            return new myResult(0,"用户名已存在","");
        }

    }
    //

}
