package com.cowave.sys.meter.domain.torna.bean;

import com.cowave.sys.meter.domain.torna.enums.DocTypeEnum;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 唯一id，接口规则：md5(module_id:parent_id:url:http_method)。
 * 分类规则：md5(module_id:parent_id:name)
 * @author tanghc
 */
public interface DocInfoDataId {

    String TPL_API = "%s:%s:%s";
    String TPL_FOLDER = "%s:%s:%s";

    default String buildDataId() {
        String content = buildDocKey();
        String version = getVersion();
        if ("-".equals(version) || version == null) {
            version = "";
        }
        content = content + version;
        return DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));
    }

    default String buildDocKey() {
        Long parentId = getParentId();
        if (parentId == null) {
            parentId = 0L;
        }
        String content;
        if (Booleans.isTrue(this.getIsGroup())) {
            content = String.format(TPL_FOLDER, getFolderId(), parentId, getName());
        } else if (!Objects.equals(getType(), DocTypeEnum.HTTP.getType())) {
            content = String.format(TPL_API, getFolderId(), getName(), DocTypeEnum.CUSTOM);
        } else {
            content = String.format(TPL_API, getFolderId(), getUrl(), getHttpMethod());
        }
        return DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));
    }

    String getName();

    Long getFolderId();

    Long getParentId();

    Byte getIsGroup();

    String getUrl();

    String getHttpMethod();

    default Byte getType() {
        return DocTypeEnum.HTTP.getType();
    }

    default String getVersion() {
        return "";
    }
}
