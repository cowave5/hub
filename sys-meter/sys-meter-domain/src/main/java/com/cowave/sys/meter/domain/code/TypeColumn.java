/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.domain.code;

import com.cowave.commons.client.http.asserts.AssertsException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@AllArgsConstructor
public class TypeColumn {

    public static final String T_INT = "int";

    public static final String T_INT2 = "int2";

    public static final String T_INT4 = "int4";

    public static final String T_INT8 = "int8";

    public static final String T_TINY_INT = "tinyint";

    public static final String T_BIG_INT = "bigint";

    public static final String T_FLOAT = "float";

    public static final String T_FLOAT4 = "float4";

    public static final String T_FLOAT8 = "float8";

    public static final String T_DOUBLE = "double";

    public static final String T_DECIMAL = "decimal";

    public static final String T_NUMERIC = "numeric";

    public static final String T_DATE = "date";

    public static final String T_DATETIME = "datetime";

    public static final String T_TIMESTAMP = "timestamp";

    public static final String T_CHAR = "char";

    public static final String T_VARCHAR = "varchar";

    public static final String T_TEXT = "text";

    public static final String T_BLOB = "blob";

    public static final String T_BYTEA = "bytea";

    public static final String T_JSON = "json";

    public static final String T_SERIAL = "serial";

    public static final String T_BIGSERIAL = "bigserial";

    public static final List<String> MYSQL = List.of(T_INT, T_TINY_INT, T_BIG_INT, T_FLOAT, T_DOUBLE,
            T_DECIMAL, T_DATE, T_DATETIME, T_TIMESTAMP, T_CHAR, T_VARCHAR, T_TEXT, T_BLOB);

    public static final List<String> OSCAR = List.of(T_INT2, T_INT4, T_INT8, T_FLOAT, T_FLOAT8,
            T_NUMERIC, T_DATE, T_TIMESTAMP, T_CHAR, T_VARCHAR, T_TEXT, T_BLOB);

    public static final List<String> POSTGRES = List.of(T_INT2, T_INT4, T_INT8, T_FLOAT4, T_FLOAT8,
            T_NUMERIC, T_DATE, T_TIMESTAMP, T_CHAR, T_VARCHAR, T_TEXT, T_BYTEA, T_SERIAL, T_BIGSERIAL, T_JSON);

    public static final List<String> JAVA_STRING = List.of(T_CHAR, T_VARCHAR, T_TEXT);

    public static final List<String> JAVA_INTEGER = List.of(T_INT, T_INT2, T_INT4, T_TINY_INT, T_SERIAL);

    public static final List<String> JAVA_LONG = List.of(T_INT8, T_BIG_INT, T_BIGSERIAL);

    public static final List<String> JAVA_FLOAT = List.of(T_FLOAT, T_FLOAT4);

    public static final List<String> JAVA_DOUBLE = List.of(T_FLOAT8, T_DOUBLE);

    public static final List<String> JAVA_BIG_DECIMAL = List.of(T_DECIMAL, T_NUMERIC);

    public static final List<String> JAVA_DATE = List.of(T_DATE, T_DATETIME, T_TIMESTAMP);

    public static final List<String> JAVA_BYTES = List.of(T_BLOB, T_BYTEA);

    public static final List<String> JAVA_MAP = List.of(T_JSON);

    public static List<String> dbTypes(String dbType) {
        return switch (dbType) {
            case "mysql" -> MYSQL;
            case "oscar" -> OSCAR;
            case "postgresql" -> POSTGRES;
            default -> throw new AssertsException("不支持的数据库类型");
        };
    }

    public static Long ofJavaType(String columnType){
        if(JAVA_STRING.contains(columnType)){
            return -1L;
        }else if(JAVA_INTEGER.contains(columnType)){
            return -2L;
        }else if(JAVA_LONG.contains(columnType)){
            return -3L;
        }else if(JAVA_FLOAT.contains(columnType)){
            return -4L;
        }else if(JAVA_DOUBLE.contains(columnType)){
            return -5L;
        }else if(JAVA_BIG_DECIMAL.contains(columnType)){
            return -6L;
        }else if(JAVA_DATE.contains(columnType)){
            return -7L;
        }else if(JAVA_BYTES.contains(columnType)){
            return -8L;
        }else if(JAVA_MAP.contains(columnType)){
            return -9L;
        }
        return null;
    }
}
