package com.example.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class CommandUsingSemaphoreIsolation extends HystrixCommand<String> {

    private final int id;


//    在当延迟是一个关注点或者线程过载是不可接受的地方，包装行为通常不访问网络。
//    executionIsolationStrategy 属性被设置为ExecutionIsolationStrategy。信号和信号隔离将被使用。
//
//    下面代码显示了该属性是如何通过代码设置为默认属性（它在运行期可以通过动态属性重置）

    public CommandUsingSemaphoreIsolation(int id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                // since we're doing an in-memory cache lookup we choose SEMAPHORE isolation
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
        this.id = id;
    }

    @Override
    protected String run() {
        // a real implementation would retrieve data from in memory data structure
        return "ValueFromHashMap_" + id;
    }

}