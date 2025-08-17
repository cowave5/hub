package com.cowave.sys.meter.infra.torna;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author tanghc
 */
@RequiredArgsConstructor
@Component
public class ManualTransactionManager {

    private final DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 手动事务处理
     * @param runner 处理业务
     * @param onError 出错时调用
     * @param <T> 返回参数类型
     */
    public <T> T execute(String transactionName, Supplier<T> runner, Consumer<Exception> onError) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName(transactionName);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            T result = runner.get();
            dataSourceTransactionManager.commit(status);
            return result;
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(status);
            onError.accept(e);
            return null;
        }
    }

}
