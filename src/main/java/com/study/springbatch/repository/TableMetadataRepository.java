package com.study.springbatch.repository;

import com.study.springbatch.vo.TableMetadata;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMetadataRepository {
    TableMetadata fetchTableMetadata();
}
