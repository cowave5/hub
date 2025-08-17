package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分享环境
 *
 * @author Joker
 * @since 1.0.0
 * @return
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareEnvironment {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 分享配置id
     */
    private Long shareConfigId;

    /**
     * 模块环境id
     */
    private Long moduleEnvironmentId;
}
