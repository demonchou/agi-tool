package com.demonchou.agi.tool.pay.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demonchou.agi.tool.pay.infra.persistence.entity.OrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper
 *
 * @author demonchou
 * @version OrderMapper, 2025/8/22 16:44 demonchou
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {
}
