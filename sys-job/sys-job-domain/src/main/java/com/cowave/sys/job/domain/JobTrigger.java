package com.cowave.sys.job.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.commons.tools.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Data
public class JobTrigger {

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 启用状态 0-停用 1-启用
	 */
	private int status = 1;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务类型
	 */
	private String taskType;

	/**
	 * 超时时间
	 */
	private int taskOverTime;

	/**
	 * 任务开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date taskBeginTime;

	/**
	 * 任务结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date taskEndTime;

	/**
	 * 脚本内容
	 */
	private String glueSource;

	/**
	 * 脚本更新时间
	 */
	private long glueUpdateTime;

	/**
	 * 调度类型
	 */
	private String triggerType;

	/**
	 * 调度参数
	 */
	private String triggerParam;

	/**
	 * 调度次数
	 */
	private long triggerTimes;

	/**
	 * 调度成功次数
	 */
	private long triggerSuccessTimes;

	/**
	 * 上次调度时间
	 */
	private long triggerLastTime;

	/**
	 * 下次调度时间
	 */
	private long triggerNextTime;

	/**
	 * 处理器名称
	 */
	private String handlerName;

	/**
	 * 处理器参数
	 */
	private String handlerParam;

	/**
	 * 过期策略
	 */
	private String misfireStrategy;

	/**
	 * 路由策略
	 */
	private String routeStrategy;

	/**
	 * 阻塞策略
	 */
	private String blockStrategy;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime = new Date();

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime = new Date();

	public String getTriggerPrev(){
		return DateUtils.format(new Date(triggerLastTime), "yyyy-mm-dd HH:MM:ss");
	}

	public String getTriggerNext(){
		return DateUtils.format(new Date(triggerNextTime), "yyyy-mm-dd HH:MM:ss");
	}
}
