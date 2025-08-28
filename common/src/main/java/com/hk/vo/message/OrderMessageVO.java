package com.hk.vo.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author huangkun
 * @date 2025/8/26 21:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderMessageVO implements Serializable {
    private static final long serialVersionUID = 4637850454798994302L;

    private Long orderId;

    private Integer orderStatus;


}
