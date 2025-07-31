package com.cowave.sys.admin.domain.constants.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.cowave.sys.admin.domain.constants.EnableStatus;
import org.apache.commons.lang3.StringUtils;

import static com.cowave.sys.admin.domain.constants.EnableStatus.DISABLE;
import static com.cowave.sys.admin.domain.constants.EnableStatus.ENABLE;

/**
 * @author shanhuiming
 */
public class EnableStatusConverter implements Converter<EnableStatus> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<EnableStatus> context) {
        EnableStatus enableStatus = context.getValue();
        if (enableStatus == null) {
            return new WriteCellData<>("");
        }
        if (ENABLE == enableStatus) {
            return new WriteCellData<>("启用");
        } else {
            return new WriteCellData<>("停用");
        }
    }

    @Override
    public EnableStatus convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String enableStatus = cellData.getStringValue();
        if (StringUtils.isBlank(enableStatus)) {
            return null;
        }
        if ("启用".equals(enableStatus)) {
            return ENABLE;
        } else {
            return DISABLE;
        }
    }
}
