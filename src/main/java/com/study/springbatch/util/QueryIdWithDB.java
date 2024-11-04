package com.study.springbatch.util;

public enum QueryIdWithDB {
    POSTGRESQL("postgresql", "com.study.springbatch.repository.TableMetadataRepository.fetchTableMetadataPostgresql"),
    ORACLE("oracle", "com.study.springbatch.repository.TableMetadataRepository.fetchTableMetadataOracle"),
    MYSQL("mysql", "com.study.springbatch.repository.TableMetadataRepository.fetchTableMetadataMysql");

    private final String database;
    private final String queryId;

    QueryIdWithDB(String database, String queryId) {
        this.database = database;
        this.queryId = queryId;
    }

    public static QueryIdWithDB fromDbType(String database) {
        for (QueryIdWithDB queryIdWithDB : values()) {
            if (queryIdWithDB.database.equalsIgnoreCase(database)) {
                return queryIdWithDB;
            }
        }
        throw new IllegalArgumentException("No enum constant for database: " + database);
    }

    public String getQueryId() {
        return queryId;
    }
}
