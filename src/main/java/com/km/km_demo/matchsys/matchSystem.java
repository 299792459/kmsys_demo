package com.km.km_demo.matchsys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.km.km_demo.dao.entity.book;
import com.km.km_demo.dao.entity.order;
import com.km.km_demo.dao.entity.scenery;
import com.km.km_demo.dao.entity.user;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import com.km.km_demo.middleware.redis.redis;
import com.km.km_demo.service.bookService;
import com.km.km_demo.service.orderService;
import com.km.km_demo.service.sceneryService;
import com.km.km_demo.service.userService;
import com.km.km_demo.util.commonUtil;
import com.km.km_demo.util.javaMailUtil;
import com.km.km_demo.util.timeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈匹配系统〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/10 0010
 * @since 1.0.0
 */
@Component
public class matchSystem {

    @Autowired
    RedisServiceImpl myRedisService;

    @Autowired
    orderService myOrderService;

    @Autowired
    javaMailUtil myJMU;

    @Autowired
    userService myUS;

    @Autowired
    sceneryService mySS;

    @Autowired
    bookService myBS;

    @Autowired
    commonUtil cu;

    //被调用时候就遍历整个redis匹配队列
    // 如果找到就匹配成功，未找到则进入阻塞等待下次唤醒。
    public void ms(String matchQueue){
        //使用缓存线程池执行匹配操作
        //这里由于线程并发，需要高度注意数据一致性的问题
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
                            //如果未被加锁
                            // 查找的时候上悲观锁（修改监视字段为0）
                            //悲观锁：redis里设置一个值作为监视字段，该字段为1时可以查，为0时不可查
                            myRedisService.set("redismatchlock","0");
                            List<book> bookList = (List<book>)myRedisService.get("matchQueue");

                            System.out.println(bookList.size());
                            //遍历booklist直到自己这一位
                            for(int i=0;i<bookList.size()-1;i++){
                                // 如果找到相同目的地的信息
                                if(bookList.get(i).getBooksceneryid()==bookList.get(bookList.size()).getBooksceneryid()){
                                    //则查看他们的时间是否匹配（也就是在同一天）
                                    // 先获取源时间
                                    String time1=bookList.get(i).getBooktime();
                                    String time2=bookList.get(bookList.size()).getBooktime();
                                    timeUtil myTimeUtil=new timeUtil();
                                    // 在原时间的基础上+1-1得到匹配范围
                                    // 如果time2满足范围则匹配成功
                                    if(myTimeUtil.timeAddNDay(-2,time1).getStatecode()==0){
                                        System.out.println("此条匹配失败，系统内时间函数转换出错");
                                    }
                                    //如果匹配不成功的话,跳过此条，继续搜索其他匹配队列
                                    if(!(myTimeUtil.timeAddNDay(-1,time1).getContent().equals(time2) ||
                                            myTimeUtil.timeAddNDay(-1,time1).getContent().equals(time2)||
                                            time1.equals(time2))){
                                        continue;
                                    }
                                    // 匹配成功则找到以后将两者信息写入order表
                                    order Norder=new order();
                                    // 匹配成功后，将二者的数据库预约信息修改
                                    book NBook1=bookList.get(i);
                                    NBook1.setBookstate("matched");
                                    book NBook2=bookList.get(bookList.size());
                                    NBook2.setBookstate("matched");
                                    myBS.update(new QueryWrapper<book>(NBook1));
                                    myBS.update(new QueryWrapper<book>(NBook2));
                                    // 从数据库中取出用户和景点信息
                                    scenery Nscenery = mySS.getById(bookList.get(i).getBooksceneryid());
                                    user user1=myUS.getById(bookList.get(i).getUserid());
                                    user user2=myUS.getById(bookList.get(bookList.size()).getUserid());

                                    Norder.setOrdersceneryid(bookList.get(i).getBooksceneryid());
                                    Norder.setOrderuserid1(user1.getUserid());
                                    Norder.setOrderuserid2(user2.getUserid());
                                    Norder.setOrdertime(new timeUtil().getNowTime());
                                    myOrderService.save(Norder);
                                    // 然后发邮件通知两位
                                    myJMU.sendmail(user1.getEmail(),
                                            "恭喜您匹配成功",
                                            "您对"+Nscenery.getSceneryname()+
                                                    "的预约已完成"+
                                                    "对象为:"+user2.getUserannoyname()+
                                                    "祝您玩的开心");
                                    myJMU.sendmail(user2.getEmail(),
                                            "恭喜您匹配成功",
                                            "您对"+Nscenery.getSceneryname()+
                                                    "的预约已完成"+
                                                    "对象为:"+user1.getUserannoyname()+
                                                    "祝您玩的开心");
                                    //最后将两者踢出匹配队列，再把新的匹配队列写入内存，释放锁
                                    //踢出队列
                                    bookList.remove(i);
                                    bookList.remove(bookList.size());
                                    //写入内存
                                    myRedisService.set("matchQueue",bookList);
                                    //释放锁
                                    myRedisService.set("redismatchlock","1");
                                    //停止查找匹配队列
                                    break;
                                }
                                //遍历完以后都没找到的话,释放锁，线程结束
                                //释放锁
                                myRedisService.set("redismatchlock","1");


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
