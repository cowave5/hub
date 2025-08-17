package com.cowave.sys.meter.domain.build;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.sys.meter.domain.constants.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author shanhuiming
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildFolder implements AccessInfoSetter {

    /**
     * 目录id
     */
    @TableId(type = IdType.AUTO)
    private Integer folderId;

    /**
     * 上级目录id
     */
    private Integer parentId;

    /**
     * 目录名称
     */
    @NotBlank(message = "目录名称不能为空")
    private String folderName;

    /**
     * 访问限制
     */
    private Visibility visibility;

    /**
     * 目录排序
     */
    private Integer folderOrder;

    /**
     * 拥有人编码
     */
    private String ownerCode;

    /**
     * 拥有人名称
     */
    private String ownerName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
