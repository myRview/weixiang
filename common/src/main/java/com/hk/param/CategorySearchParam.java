package com.hk.param;

import com.hk.common.PageBaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangkun
 * @date 2025/7/29 14:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategorySearchParam extends PageBaseVO {

    private String categoryName;
}
