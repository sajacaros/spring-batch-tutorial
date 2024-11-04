package com.study.springbatch.economic.processor;

import com.study.springbatch.vo.EconomicIndex;
import com.study.springbatch.vo.EconomicIndexChangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class AggregateEconomicProcessor implements ItemProcessor<EconomicIndex, EconomicIndexChangeRate> {

    @Override
    public EconomicIndexChangeRate process(EconomicIndex item) throws Exception {
        log.info("economic index : {}", item);
        EconomicIndexChangeRate economicIndexChangeRate = new EconomicIndexChangeRate();
        economicIndexChangeRate.setName(item.getName());
        economicIndexChangeRate.setValue1((item.getValue2()-item.getValue1())/item.getValue1());
        economicIndexChangeRate.setValue2((item.getValue3()-item.getValue2())/item.getValue2());
        economicIndexChangeRate.setValue3((item.getValue4()-item.getValue3())/item.getValue3());
        economicIndexChangeRate.setValue4((item.getValue5()-item.getValue4())/item.getValue4());
        economicIndexChangeRate.setValue5((item.getValue6()-item.getValue5())/item.getValue5());
        log.info("economic index rate : {}", economicIndexChangeRate);
        return economicIndexChangeRate;
    }
}
