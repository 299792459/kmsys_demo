package com.km.km_demo.dao.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈个人主页留言表〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/9 0009
 * @since 1.0.0
 */
public class letterVO implements Serializable {


    //网络传输数据持久化
    private static final long serialVersionUID = 202004091650L;

    Integer letterid;
    Integer userid;
    Integer letteruserid;
    String lettercontent;
    String time;
    String state;
    String other;

    public letterVO() {
    }

    @Override
    public String toString() {
        return "letter{" +
                "letterid=" + letterid +
                ", userid=" + userid +
                ", letteruserid=" + letteruserid +
                ", lettercontent='" + lettercontent + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                ", other='" + other + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof letterVO)) return false;
        letterVO letter = (letterVO) o;
        return getLetterid().equals(letter.getLetterid()) &&
                Objects.equals(getUserid(), letter.getUserid()) &&
                Objects.equals(getLetteruserid(), letter.getLetteruserid()) &&
                Objects.equals(getLettercontent(), letter.getLettercontent()) &&
                Objects.equals(getTime(), letter.getTime()) &&
                Objects.equals(getState(), letter.getState()) &&
                Objects.equals(getOther(), letter.getOther());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLetterid(), getUserid(), getLetteruserid(), getLettercontent(), getTime(), getState(), getOther());
    }

    public letterVO(Integer letterid, Integer userid, Integer letteruserid, String lettercontent, String time, String state, String other) {
        this.letterid = letterid;
        this.userid = userid;
        this.letteruserid = letteruserid;
        this.lettercontent = lettercontent;
        this.time = time;
        this.state = state;
        this.other = other;
    }

    public Integer getLetterid() {
        return letterid;
    }

    public void setLetterid(Integer letterid) {
        this.letterid = letterid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getLetteruserid() {
        return letteruserid;
    }

    public void setLetteruserid(Integer letteruserid) {
        this.letteruserid = letteruserid;
    }

    public String getLettercontent() {
        return lettercontent;
    }

    public void setLettercontent(String lettercontent) {
        this.lettercontent = lettercontent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
