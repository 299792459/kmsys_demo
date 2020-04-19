package com.km.km_demo.util;

import com.km.km_demo.middleware.redis.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈公共工具类〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/7 0007
 * @since 1.0.0
 */
@Component
public class commonUtil {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    RedisServiceImpl myRedis;

    /**
     //测试函数
     public String test(){
     System.out.println("        "+myRedis.get("cjk2").toString());
     return null;
     }**/


     //通用函数：验证邮箱
     public myResult getverifyCode(String verifycode,String Email){
     //System.out.println("        diaoyongchengg ");

     try {
     if(verifycode.equals(myRedis.get(Email).toString())){
        return new myResult(1,"邮箱验证通过","");
     }
     return new myResult(0,"邮箱验证未通过","");
     }catch (Exception e){
     e.printStackTrace();
     }
     return new myResult(0,"未知错误，请联系管理员","");

     }

    //通用函数：验证锁
    //如果被上了锁则返回0，表示不可操作
    public int checklock(String lockname){
        if(myRedis.get("lockname").equals("0")){
            return 0;
        }
        return 1;
    }

}
