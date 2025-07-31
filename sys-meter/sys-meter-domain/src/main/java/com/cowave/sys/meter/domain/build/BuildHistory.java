package com.cowave.sys.meter.domain.build;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildHistory {

    /**
     * 构建id
     */
    @TableId(type = IdType.AUTO)
    private Long historyId;

    /**
     * 任务id
     */
    private Integer buildId;

    /**
     * 构建序列
     */
    private Long buildIndex;

    /**
     * 构建目录
     */
    private String buildHome;

    /**
     * 构建分支
     */
    private String buildBranch;

    /**
     * 构建标签
     */
    private String buildTag;

    /**
     * 构建版本
     */
    private String buildVersion;

    /**
     * 构建耗时
     */
    private long buildCost;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
