/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.constants.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.cowave.sys.admin.domain.constants.YesNo;
import org.apache.commons.lang3.StringUtils;

import static com.cowave.sys.admin.domain.constants.YesNo.NO;
import static com.cowave.sys.admin.domain.constants.YesNo.YES;

/**
 * @author shanhuiming
 */
public class YesNoConverter implements Converter<YesNo> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<YesNo> context) {
        YesNo yesNo = context.getValue();
        if (yesNo == null) {
            return new WriteCellData<>("");
        }
        if (YES == yesNo) {
            return new WriteCellData<>("是");
        } else {
            return new WriteCellData<>("否");
        }
    }

    @Override
    public YesNo convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String yesNo = cellData.getStringValue();
        if (StringUtils.isBlank(yesNo)) {
            return null;
        }
        if ("是".equals(yesNo) || "Yes".equalsIgnoreCase(yesNo)) {
            return YES;
        } else {
            return NO;
        }
    }
}
