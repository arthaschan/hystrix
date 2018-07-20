package com.example.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;


/**
 * 失败后回滚
 */
public class CommandHelloFailure extends HystrixCommand<String> {

    private final String name;

    public CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        //抛异常，确保run 总是失败

//        错误传播
//        从run（）方法中抛出的所以异常(除了HystrixBadRequestException)都被计为异常。
//        将触发getFallback()和熔断逻辑。在HystrixBadRequestException中抛出的例外，你可以根据你的喜好进行包装，然后通过getCause()获取。
//        HystrixBadRequestException设计的使用场景为，报告不合法的参数或非系统性错误。这些都不能计入失败次数的度量，也不应当触发回退逻辑
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        //失败后执行getFallback  方法
        return "Hello Failure " + name + "!";
    }
}