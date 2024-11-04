package com.study.springbatch.economic;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.IOException;
import java.io.Writer;

public class EconomicIndexHeaderHandler implements FlatFileHeaderCallback {
    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write("지수명,24.03~24.04,24.04~24.05,24.05~24.06,24.06~24.07,24.07~24.08");
    }

}
