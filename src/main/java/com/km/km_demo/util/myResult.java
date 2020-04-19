package com.km.km_demo.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈自定义返回的数据类型〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/3/14 0014
 * @since 1.0.0
 */

import lombok.Data;
import org.springframework.stereotype.Component;


@Component
public class myResult implements Serializable {

    //网络传输序列化
    private static final long serialVersionUID = 202004101359L;

    int statecode;
    String message;
    Object content;

    public myResult() {
    }

    public myResult(int statecode, String message, Object content) {
        this.statecode = statecode;
        this.message = message;
        this.content = content;
    }

    @Override
    public String toString() {
        return "myResult{" +
                "statecode=" + statecode +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof myResult)) return false;
        myResult myResult = (myResult) o;
        return getStatecode() == myResult.getStatecode() &&
                getMessage().equals(myResult.getMessage()) &&
                getContent().equals(myResult.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatecode(), getMessage(), getContent());
    }

    public int getStatecode() {
        return statecode;
    }

    public void setStatecode(int statecode) {
        this.statecode = statecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
