package com.study.springbatch.economic;

import com.study.springbatch.vo.EconomicIndexChangeRate;
import org.springframework.batch.item.file.transform.LineAggregator;

public class EconomicIndexAggregator implements LineAggregator<EconomicIndexChangeRate> {
    @Override
    public String aggregate(EconomicIndexChangeRate item) {
        return item.representation();
    }
}
