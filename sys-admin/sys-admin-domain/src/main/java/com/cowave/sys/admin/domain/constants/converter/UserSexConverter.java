package com.cowave.sys.admin.domain.constants.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.cowave.sys.admin.domain.constants.UserSex;
import org.apache.commons.lang3.StringUtils;

import static com.cowave.sys.admin.domain.constants.UserSex.*;

/**
 * @author shanhuiming
 */
public class UserSexConverter implements Converter<UserSex> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<UserSex> context) {
        UserSex userSex = context.getValue();
        if(userSex == null){
            return new WriteCellData<>("");
        }
        return switch(userSex) {
            case MALE -> new WriteCellData<>("男");
            case FEMALE -> new WriteCellData<>("女");
            default -> new WriteCellData<>("未知");
        };
    }

    @Override
    public UserSex convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String userSex = cellData.getStringValue();
        if(StringUtils.isBlank(userSex)){
            return null;
        }
        return switch (userSex) {
            case "男" -> MALE;
            case "女" -> FEMALE;
            default -> UNKNOWN;
        };
    }
}
