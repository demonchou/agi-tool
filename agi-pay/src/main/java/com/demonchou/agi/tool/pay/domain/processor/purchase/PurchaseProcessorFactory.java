package com.demonchou.agi.tool.pay.domain.processor.purchase;

import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.enums.ProductTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 购买策略工厂
 *
 * @author demonchou
 * @version PurchaseProcessorFactory, 2025/8/22 15:32 demonchou
 */
@Service
public class PurchaseProcessorFactory implements InitializingBean {

    @Autowired
    private List<PayProcessor> payProcessorList;

    private static class ProcessorKey {
        final PayChannelEnum payChannel;
        final ProductTypeEnum productType;

        ProcessorKey(PayChannelEnum payChannel, ProductTypeEnum productType) {
            this.payChannel = payChannel;
            this.productType = productType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProcessorKey that = (ProcessorKey) o;
            return payChannel == that.payChannel && productType == that.productType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(payChannel, productType);
        }
    }

    private final Map<ProcessorKey, PayProcessor> payProcessorMap = new HashMap<>();

    public PayProcessor getProcessor(PayChannelEnum channel, ProductTypeEnum productType) {
        ProcessorKey key = new ProcessorKey(channel, productType);
        PayProcessor payProcessor = payProcessorMap.get(key);
        if (payProcessor == null) {
            throw new IllegalArgumentException("No purchase processor found for channel: " + channel + ", productType: " + productType);
        }
        return payProcessor;
    }

    @Override
    public void afterPropertiesSet() {
        for (PayProcessor payProcessor : payProcessorList) {
            ProcessorKey key = new ProcessorKey(payProcessor.getSupportedChannel(), payProcessor.getSupportedGoodsType());
            // 检查是否已存在相同键的策略 (避免冲突)
            if (payProcessorMap.containsKey(key)) {
                throw new IllegalStateException("Duplicate strategy found for channel: " + key.payChannel + ", productType: " + key.productType);
            }

            payProcessorMap.put(key, payProcessor);
        }
        if (payProcessorMap.isEmpty()) {
            System.out.println("No purchase processor found.");
            throw new IllegalStateException("No purchase processor found.");
        }
        System.out.println("Initialized PurchaseProcessorFactory with " + payProcessorMap.size() + " processors.");
    }
}
