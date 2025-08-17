package com.cowave.hub.admin.domain.rabc.enums.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.cowave.hub.admin.domain.rabc.enums.SuccessStatus;

import static com.cowave.hub.admin.domain.rabc.enums.SuccessStatus.SUCCESS;

/**
 * @author shanhuiming
 */
public class SuccessStatusConverter implements Converter<SuccessStatus> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<SuccessStatus> context) {
        SuccessStatus successStatus = context.getValue();
        if (successStatus == null) {
            return new WriteCellData<>("");
        }
        if (SUCCESS == successStatus) {
            return new WriteCellData<>("成功");
        } else {
            return new WriteCellData<>("失败");
        }
    }
}
