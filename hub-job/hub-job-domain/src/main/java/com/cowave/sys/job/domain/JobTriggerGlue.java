package com.cowave.sys.job.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Data
public class JobTriggerGlue {

	private Integer id;

	private int triggerId;

	private String glueType;

	private String glueSource;

	private String remark;

	private Date createTime;

	private Date updateTime;
}
