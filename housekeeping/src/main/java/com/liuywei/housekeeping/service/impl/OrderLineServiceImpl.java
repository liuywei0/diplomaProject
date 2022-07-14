package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.OrderLineMapper;
import com.liuywei.housekeeping.dao.extend.OrderLineExtendMapper;
import com.liuywei.housekeeping.domain.OrderLine;
import com.liuywei.housekeeping.domain.OrderLineExample;
import com.liuywei.housekeeping.domain.extend.OrderLineExtend;
import com.liuywei.housekeeping.service.IOrderLineService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderLineServiceImpl implements IOrderLineService {

    @Resource
    private OrderLineMapper orderLineMapper;

    @Resource
    private OrderLineExtendMapper orderLineExtendMapper;

    /**
     * 查询所有订单项
     *
     * @return
     */
    @Override
    public List<OrderLine> findAll() {
        return orderLineMapper.selectByExample(new OrderLineExample());
    }

    /**
     * 根据id查询订单项
     *
     * @param id
     * @return
     */
    @Override
    public OrderLine findById(long id) {
        return orderLineMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存或修改订单项信息
     *
     * @param orderLine
     */
    @Override
    public void saveOrUpdate(OrderLine orderLine) throws CustomException {
        if (orderLine.getId() != null) {
            orderLineMapper.updateByPrimaryKey(orderLine);
        } else {
            orderLineMapper.insert(orderLine);
        }
    }

    /**
     * 根据id删除订单项
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {

        if (orderLineMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败，未查询到要删除的数据");
        }

        orderLineMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过id查询现有产品信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderLineExtend> findByOrderId(long orderId) {
        return orderLineExtendMapper.selectByOrderId(orderId);
    }
}
