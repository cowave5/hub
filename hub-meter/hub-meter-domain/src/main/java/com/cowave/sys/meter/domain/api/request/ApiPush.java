package com.cowave.sys.meter.domain.api.request;

import com.cowave.sys.meter.domain.torna.param.DocParamPushParam;
import com.cowave.sys.meter.domain.torna.param.DubboParam;
import com.cowave.sys.meter.domain.torna.param.HeaderParamPushParam;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * @author tanghc
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ApiPush {

    /**
     * 文档名称
     */
    @Length(max = 64, message = "文档名称长度不能超过64")
    @NotBlank(message = "文档名称不能为空")
    private String name;

    /**
     * 文档描述
     */
    private String description;

    /**
     * 接口维护人
     */
    private String author;

    /**
     * 废弃描述，有值代表废弃
     */
    @Length(max = 128, message = "'deprecated' 长度不能超过128")
    private String deprecated;

    /**
     * 0:http,1:dubbo,2:富文本,3:markdown
     */
    private Byte type;

    /**
     * 请求url
     */
    @Length(max = 200, message = "'url' 长度不能超过200")
    private String url;

    /**
     * 版本号
     */
    @Length(max = 32, message = "'version' 长度不能超过32")
    private String version = "";

    /**
     * dubbo的接口定义
     */
    @Length(max = 200, message = "'definition' 长度不能超过200")
    private String definition;

    /**
     * http方法
     */
    @Length(max = 12, message = "'httpMethod' 长度不能超过12")
    private String httpMethod;

    /**
     * contentType
     */
    @Length(max = 128, message = "'contentType' 长度不能超过128")
    private String contentType;

    /**
     * 是否是文件夹，1：是，0：否
     */
    private Byte isFolder;

    /**
     * 是否显示，1：显示，0：不显示
     */
    private Byte isShow;

    /**
     * 排序
     */
    private Integer orderIndex;

    /**
     * 是否请求数组
     */
    private Byte isRequestArray = 0;

    /**
     * 是否返回数组
     */
    private Byte isResponseArray = 0;

    /**
     * 请求数组元素类型, object/number/string/boolean
     */
    @Builder.Default
    private String requestArrayType = "object";

    /**
     * 返回数组元素类型, object/number/string/boolean
     */
    @Builder.Default
    private String responseArrayType = "object";

    /**
     * dubbo服务信息
     */
    private DubboParam dubboInfo;

    /**
     * path参数
     */
    private List<DocParamPushParam> pathParams;

    /**
     * 请求头
     */
    private List<HeaderParamPushParam> headerParams;

    /**
     * Query参数
     */
    private List<DocParamPushParam> queryParams;

    /**
     * Body参数
     */
    private List<DocParamPushParam> requestParams;

    /**
     * 返回参数
     */
    private List<DocParamPushParam> responseParams;

    /**
     * 错误码
     */
    private List<ApiCodePush> errorCodeParams;

    /**
     * 下级文档
     */
    private List<ApiPush> items;

    private String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiPush that = (ApiPush) o;
        return name.equals(that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(httpMethod, that.httpMethod) &&
                Objects.equals(isFolder, that.isFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, httpMethod, isFolder);
    }
}
