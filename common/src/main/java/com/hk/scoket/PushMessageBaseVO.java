package com.hk.scoket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/8/5 22:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessageBaseVO<T> implements Serializable {
    private static final long serialVersionUID = -6554371572977252626L;
    //消息类型
    private String type;
    //消息内容
    private T data;
    //发送时间
    private Date date;


    public PushMessageBaseVO(String type, T data) {
        this.type = type;
        this.data = data;
    }
}
