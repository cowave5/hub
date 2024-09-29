/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.entity;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 库表字段
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DbTableColumn extends AccessInfo {

    /**
     * 表id
     */
    private Long tableId;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * id
     */
    private Long id;

    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段注释
     */
    private String columnComment;

    /**
     * 字段类型
     */
    private String columnTypename;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 字段长度 oscar
     */
    private Integer columnLength;

    /**
     * 字段长度
     */
    private Integer columnPrecision;

    /**
     * 字段精度
     */
    private Integer columnScale;

    /**
     * date长度
     */
    private Integer datetimePrecision;

    /**
     * 字段默认值
     */
    private String columnDefault;

    /**
     * 字段排序
     */
    private Integer sort;

    /**
     * 是否主键
     */
    private Integer isPrimary;

    /**
     * 是否非空
     */
    private Integer isNotnull;

    /**
     * 是否自增
     */
    private Integer isIncrement;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public ModelField toField() {
        ModelField field = new ModelField();
        field.setFieldName(toCamel(columnName));
        field.setFieldType(TypeColumn.ofJavaType(columnType));
        field.setFieldComment(columnComment);
        field.setSort(sort);
        field.setIsNotnull(isNotnull);
        field.setIsCollect(0);
        field.setIsExcel(0);
        field.setExcelWidth(20);
        field.setColumnId(id);
        field.setIsWhere(0);
        field.setIsList(1);
        field.setIsInfo(1);
        field.setIsInsert(1);
        field.setIsEdit(1);
        return field;
    }

    private String toCamel(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public boolean getQuotmark() {
        if (columnType == null) {
            return true;
        }
        return columnDefault != null && !columnDefault.startsWith("'")
                && (columnTypename.startsWith("varchar") || columnTypename.startsWith("text") || columnTypename.startsWith("char"));
    }

    public void parseOscarColumn() {
        this.columnName = this.columnName.toLowerCase();
        this.columnType = this.columnType.toLowerCase();
        if (columnType.startsWith("varchar")) {
            this.columnPrecision = this.columnLength;
            this.columnTypename = this.columnType + "(" + this.columnPrecision + ")";
        } else if (this.columnType.startsWith("bpchar")) {
            this.columnPrecision = this.columnLength;
            this.columnTypename = "char" + "(" + this.columnPrecision + ")";
            this.columnType = "char";
        } else if (this.columnType.startsWith("numeric")) {
            this.columnTypename = this.columnType + "(" + this.columnPrecision + "," + this.columnScale + ")";
        } else if (this.columnType.startsWith("hpfloat") || this.columnType.startsWith("lpfloat")) {
            this.columnPrecision = null;
            this.columnTypename = "float";
            this.columnType = "float";
        } else if (this.columnType.startsWith("timestamp")) {
            this.columnType = "timestamp";
            this.columnTypename = "timestamp";
        }else {
            this.columnTypename = this.columnType;
        }

        if(this.columnDefault != null){
            this.columnDefault = this.columnDefault.toLowerCase();
        }

        if(this.columnComment != null && this.columnComment.length() > 255){
            this.columnComment = this.columnComment.substring(0, 255);
        }
    }

    public void parseMysqlColumn() {
        if(this.columnTypename.startsWith("varchar")){
            this.columnType = "varchar";
            this.columnPrecision = Integer.parseInt(columnTypename.substring(columnTypename.indexOf("(") + 1, columnTypename.indexOf(")")));
        }else if(this.columnTypename.startsWith("char")){
            this.columnType = "char";
            this.columnPrecision = Integer.parseInt(columnTypename.substring(columnTypename.indexOf("(") + 1, columnTypename.indexOf(")")));
        }else if(this.columnTypename.startsWith("decimal")){
            this.columnType = "decimal";
            this.columnPrecision = Integer.parseInt(columnTypename.substring(columnTypename.indexOf("(") + 1, columnTypename.indexOf(",")));
            this.columnScale = Integer.parseInt(columnTypename.substring(columnTypename.indexOf(",") + 1, columnTypename.indexOf(")")));
        }else if(this.columnTypename.startsWith("datetime")){
            this.columnType = "datetime";
            if(this.columnTypename.contains("(")){
                this.columnPrecision = Integer.parseInt(columnTypename.substring(columnTypename.indexOf("(") + 1, columnTypename.indexOf(")")));
            }else{
                this.columnPrecision = 6;
            }
        }else if(this.columnTypename.startsWith("timestamp")){
            this.columnType = "timestamp";
        }else {
            this.columnType = this.columnTypename;
        }

        if(this.columnDefault != null){
            this.columnDefault = this.columnDefault.toLowerCase();
        }

        if(this.columnComment != null && this.columnComment.length() > 255){
            this.columnComment = this.columnComment.substring(0, 255);
        }
    }

    public void parsePostgresColumn() {
        if (columnType.startsWith("varchar")) {
            this.columnPrecision = this.columnLength;
            this.columnTypename = this.columnType + "(" + this.columnPrecision + ")";
        } else if (this.columnType.startsWith("bpchar")) {
            this.columnPrecision = this.columnLength;
            this.columnTypename = "char" + "(" + this.columnPrecision + ")";
            this.columnType = "char";
        } else if (this.columnType.startsWith("numeric")) {
            this.columnTypename = this.columnType + "(" + this.columnPrecision + "," + this.columnScale + ")";
        } else if (this.columnType.startsWith("timestamp")) {
            this.columnTypename = "timestamp";
        } else if (this.columnType.equals(TypeColumn.T_INT4) && this.isIncrement == 1){
            this.columnTypename = TypeColumn.T_SERIAL;
            this.columnDefault = null;
        } else if (this.columnType.equals(TypeColumn.T_INT8) && this.isIncrement == 1){
            this.columnTypename = TypeColumn.T_BIGSERIAL;
            this.columnDefault = null;
        } else {
            this.columnTypename = this.columnType;
        }

        if(this.columnDefault != null){
            this.columnDefault = this.columnDefault.toLowerCase();
        }

        if(this.columnComment != null && this.columnComment.length() > 255){
            this.columnComment = this.columnComment.substring(0, 255);
        }
    }

    public String getOscarType() {
        if("oscar".equals(dbType)){
            return columnTypename;
        }
        if(this.columnType.startsWith("decimal")){
            return "numeric(" + this.columnPrecision + "," + this.columnScale + ")";
        }
        if(this.columnType.startsWith("datetime")){
            return "timestamp";
        }
        if(this.columnType.equals("double")){
            return "float8";
        }
        if(this.columnType.equals("int")){
            return "int4";
        }
        if(this.columnType.startsWith("tinyint")){
            return "int2";
        }
        if(this.columnType.startsWith("bigint")){
            return "int8";
        }
        return columnTypename;
    }

    public String getMysqlType() {
        if(dbType.equals("mysql")){
            return columnTypename;
        }
        if(this.columnType.startsWith("numeric")){
            return "decimal(" + this.columnPrecision + "," + this.columnScale + ")";
        }
        if(this.columnType.equals("float8")){
            return "double";
        }
        if(this.columnType.equals("in4")){
            return "int";
        }
        if(this.columnType.equals("int2")){
            return "tinyint";
        }
        if(this.columnType.equals("int8")){
            return "bigint";
        }
        return columnTypename;
    }

    public String getPgType() {
        if(dbType.equals("postgresql")){
            return columnTypename;
        }
        if(this.columnType.startsWith("decimal")){
            return "numeric(" + this.columnPrecision + "," + this.columnScale + ")";
        }
        if(this.columnType.startsWith("datetime")){
            return "timestamp";
        }
        if(this.columnType.equals("double")){
            return "float8";
        }
        if(this.columnType.equals("int")){
            return "int4";
        }
        if(this.columnType.startsWith("tinyint")){
            return "int2";
        }
        if(this.columnType.startsWith("bigint")){
            return "int8";
        }
        return columnTypename;
    }

    public String getOscarDefault() {
        if(this.columnDefault == null){
            return null;
        }
        return columnDefault;
    }

    public String getMysqlDefault() {
        if(this.columnDefault == null){
            return null;
        }
        return columnDefault;
    }

    public String getPostgresDefault() {
        if(this.columnDefault == null){
            return null;
        }
        return columnDefault;
    }
}
