package com.cowave.sys.meter.domain.api.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.sys.meter.domain.torna.enums.OperationMode;
import com.cowave.sys.meter.domain.torna.bean.Booleans;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author tanghc
 */
@Data
public class ApiCodePush {

    /**
     * 错误码
     */
    @JSONField(name = "code")
    @Length(max = 50, message = "参数名不能超过50")
    @NotBlank(message = "参数名不能为空")
    private String name;

    /**
     * 错误描述
     */
    @JSONField(name = "msg")
    @Length(max = 256, message = "描述长度不能超过256")
    @NotBlank(message = "错误描述不能为空")
    private String description;

    /**
     * 解决方案
     */
    @JSONField(name = "solution")
    @Length(max = 100, message = "描述长度不能超过100")
    private String example = "";

    private Byte createMode = OperationMode.OPEN.getType();

    private Byte modifyMode = OperationMode.OPEN.getType();

    private Byte isDeleted = Booleans.FALSE;

}
