package com.cowave.sys.job.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Data
public class JobTriggerQuery {

    /**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务类型
	 */
	private String taskType;

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
}
