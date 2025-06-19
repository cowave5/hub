package com.cowave.sys.job.client.handler;

/**
 * @author xuxueli/shanhuiming
 */
public interface JobHandler {

	void execute() throws Exception;

	default void init() throws Exception {

	}

	default void destroy() throws Exception {

	}
}
