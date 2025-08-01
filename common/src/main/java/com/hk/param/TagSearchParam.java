package com.hk.param;

import com.hk.common.PageBaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangkun
 * @date 2025/7/30 8:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagSearchParam extends PageBaseVO {
    private String tagName;
}
