package com.km.km_demo.dao.vo;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户表展示的字段〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/5 0005
 * @since 1.0.0
 */
@Data
public class sceneryVO {
    //网络传输数据持久化
    private static final long serialVersionUID = 202004051852L;


    int sceneryid;
    String sceneryname;
    String scenerydescription;
    String scenerystate;
}
