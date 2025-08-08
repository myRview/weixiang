package com.hk.scoket;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/8/5 22:28
 */
@Data
public class BaseMessage implements Serializable {
    private static final long serialVersionUID = -6554371572977252626L;
    //消息id
    private Long id;
    //消息内容
    private String message;
    //发送人
    private String fromUserId;
    //接收人
    private String toUserId;
    //发送时间
    private Date date;

}
