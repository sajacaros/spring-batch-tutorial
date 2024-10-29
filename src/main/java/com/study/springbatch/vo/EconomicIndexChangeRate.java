package com.study.springbatch.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EconomicIndexChangeRate {
    private String name;
    private float value1, value2, value3, value4, value5;

    public String representation() {
        return String.format("%s%%,%.2f%%,%.2f%%,%.2f%%,%.2f%%,%.2f%%", name, value1*100, value2*100, value3*100, value4*100, value5*100);
    }
}
