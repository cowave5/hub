package com.cowave.sys.job.client;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.client.handler.JobHandler;
import com.cowave.sys.job.domain.constant.TriggerStatusEnum;
import com.cowave.sys.job.domain.client.TriggerResponse;
import com.cowave.sys.job.domain.client.TriggerRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
@Slf4j
public class JobThread extends Thread{

	private final LinkedBlockingQueue<TriggerRequest> triggerQueue = new LinkedBlockingQueue<>();

	private final Set<Long> triggerLogIdSet = Collections.synchronizedSet(new HashSet<>());

	private final int jobId;

    private final JobHandler jobHandler;

	private volatile boolean toStop = false;

    private boolean running = false;    // if running job

	private int idleTimes = 0;			// idle times

	public JobThread(int jobId, JobHandler jobHandler) {
		this.jobId = jobId;
		this.jobHandler = jobHandler;
		this.setName("Job-" + jobId);
	}

    public Response<String> pushTriggerQueue(TriggerRequest triggerRequest) {
		if (triggerLogIdSet.contains(triggerRequest.getLogId())) {
			return Response.error("repeate trigger job, " + triggerRequest.getLogId());
		}
		triggerLogIdSet.add(triggerRequest.getLogId());
		triggerQueue.add(triggerRequest);
        return Response.success();
	}

	public void toStop() {
		this.toStop = true;
	}

    public boolean isRunningOrHasQueue() {
        return running || !triggerQueue.isEmpty();
    }

    @Override
	public void run() {
    	try {
			jobHandler.init();
		} catch (Throwable e) {
    		log.error("", e);
		}

		while(!toStop){
			running = false;
			idleTimes++;
            TriggerRequest triggerRequest = null;
            try {
				// to check toStop signal, we need cycle, so we cannot use queue.take(), instead of poll(timeout)
				triggerRequest = triggerQueue.poll(3L, TimeUnit.SECONDS);
				if (triggerRequest !=null) {
					running = true;
					idleTimes = 0;
					triggerLogIdSet.remove(triggerRequest.getLogId());

					// init job context
					TriggerContext triggerContext = new TriggerContext(
							triggerRequest.getTriggerId(),
							triggerRequest.getHandlerParams(),
							triggerRequest.getBroadIndex(),
							triggerRequest.getBroadTotal());
					TriggerContext.set(triggerContext);

					if (triggerRequest.getTimeOut() > 0) {
						// limit timeout
						Thread futureThread = null;
						try {
							FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
                                TriggerContext.set(triggerContext);
                                jobHandler.execute();
                                return true;
                            });
							futureThread = new Thread(futureTask);
							futureThread.start();
							futureTask.get(triggerRequest.getTimeOut(), TimeUnit.SECONDS);
						} catch (TimeoutException e) {
							TriggerContext.get().setHandleStatus(TriggerStatusEnum.EXEC_OVERTIME.getStatus());
						} finally {
							futureThread.interrupt();
						}
					} else {
						// just execute
						jobHandler.execute();
					}
				} else {
					if (idleTimes > 30) {
						if(triggerQueue.isEmpty()) {
							JobContext.delJobThread(jobId);
						}
					}
				}
			} catch (Throwable e) {
				log.error("", e);
			} finally {
                if(triggerRequest != null) {
                    if (!toStop) {
						TriggerResponse triggerResponse = new TriggerResponse();
						triggerResponse.setLogId(triggerRequest.getLogId());
						triggerResponse.setTriggerId(triggerRequest.getTriggerId());
						triggerResponse.setHandleStatus(TriggerContext.get().getHandleStatus());
						triggerResponse.setHandleTime(TriggerContext.get().getHandleTime());
						triggerResponse.setHandleCost(TriggerContext.get().getHandleCost());
						triggerResponse.setHandleMsg(TriggerContext.get().getHandleMsg());
                        ClientCallbackHandler.pushCallBack(triggerResponse);
                    } else {
                        // is killed
						TriggerResponse triggerResponse = new TriggerResponse();
						triggerResponse.setLogId(triggerRequest.getLogId());
						triggerResponse.setTriggerId(triggerRequest.getTriggerId());
						triggerResponse.setHandleStatus(TriggerStatusEnum.EXEC_FAIL.getStatus());
						triggerResponse.setHandleTime(TriggerContext.get().getHandleTime());
						triggerResponse.setHandleCost(TriggerContext.get().getHandleCost());
						triggerResponse.setHandleMsg("client killed");
						ClientCallbackHandler.pushCallBack(triggerResponse);
                    }
                }
            }
        }

		while (triggerQueue != null && !triggerQueue.isEmpty()) {
			TriggerRequest triggerRequest = triggerQueue.poll();
			if (triggerRequest != null) {
				TriggerResponse triggerResponse = new TriggerResponse();
				triggerResponse.setLogId(triggerRequest.getLogId());
				triggerResponse.setTriggerId(triggerRequest.getTriggerId());
				triggerResponse.setHandleStatus(TriggerStatusEnum.EXEC_FAIL.getStatus());
				triggerResponse.setHandleTime(TriggerContext.get().getHandleTime());
				triggerResponse.setHandleCost(TriggerContext.get().getHandleCost());
				triggerResponse.setHandleMsg("client killed in queue");
				ClientCallbackHandler.pushCallBack(triggerResponse);
			}
		}

		try {
			jobHandler.destroy();
		} catch (Throwable e) {
			log.error("", e);
		}
	}
}
