package com.study.springbatch.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EconomicIndex {
    private String name;
    private float value1, value2, value3, value4, value5, value6;
}