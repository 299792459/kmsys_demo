package com.km.km_demo.controller.pagecontroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.km.km_demo.dao.entity.user;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.service.userService;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.javaMailUtil;
import com.km.km_demo.util.myResult;
import com.km.km_demo.util.myrandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈登录页面〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/5 0005
 * @since 1.0.0
 */

@RestController
@CrossOrigin
@RequestMapping("/login")
@EnableAutoConfiguration
public class loginController {

    @Autowired
    userService myUserService;

    @Autowired
    javaMailUtil myjavaMailUtil;

    @Autowired
    RedisServiceImpl myRedis;

    @Autowired
    commonUtil cu;

    //用户输入用户名，密码，传入后台
    @RequestMapping("/user")
    public myResult userLogin(@RequestParam(value="username")String username,@RequestParam(value="password")String password){

        System.out.println("成功被调用");
        user Nuser=new user();
        //后台首先通过用户名查找是否存在，不存在则返回用户不存在
        try{
            Nuser=myUserService.getOne(new QueryWrapper<user>().eq("useraccountname",username));
            if(Nuser==null) {
                return new myResult(0,"用户名不存在",null);
            }
            else{
                //如果用户名存在，判断其状态，如果不是正常，则返回对应状态
                if(Nuser.getUserstate()==null||Nuser.getUserstate().equals("")){
                    //如果用户名存在且正常，查找密码是否匹配，匹配则跳转，不匹配返回密码错误。
                    if(Nuser.getUserpassword().equals(password)){
                        return new myResult(1,"登陆成功，欢迎您:"+Nuser.getUserannoyname(),Nuser);
                    }
                    return new myResult(0,"对不起，您的密码错误,请重新输入",null);
                }
                 }
        }catch(Exception e){
                e.printStackTrace();
            }
            return new myResult(0,"对不起，您的账号："+Nuser.getUserstate()+"状态异常，请联系管理员",null);
    }


    //管理员输入账号，并通过邮箱验证登录
    @RequestMapping("/admin")
    public myResult adminLogin(@RequestParam(value="username")String username,@RequestParam(value="email")String email){

        //后台首先通过账号查找是否存在，不存在则返回不存在该管理员
        user Nuser=myUserService.getOne(new QueryWrapper<user>().eq("useraccountname",username).like("userstate","admin"));

        if(Nuser==null) {
            return new myResult(0,"该管理员不存在",null);
        }
        else{
            //如果存在的话，则接下来验证邮箱
            return new myResult(1,"请输入验证码:",null);
        }
    }

    //管理员验证邮箱
    @RequestMapping("verifycode")
    public myResult verifyCode(@RequestParam(value="Email")String Email,@RequestParam(value="verifycode")String verifycode){
        return cu.getverifyCode(verifycode,Email);
    }




}
