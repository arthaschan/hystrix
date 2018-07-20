package com.example.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandThatFailsSilently extends HystrixCommand<String> {



    //
//    无声失败
//
//    无声的失败等同于返回一个空的响应或者删除功能,它通过返回null，空的map对象，空的list或者其他类似的响应实现。
//    通常通过HystrixCommand实例中的getFallback() 方法实现

    private final boolean throwException;

    public CommandThatFailsSilently(boolean throwException) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.throwException = throwException;
    }

    @Override
    protected String run() {
        if (throwException) {
            throw new RuntimeException("failure from CommandThatFailsFast");
        } else {
            return "success";
        }
    }

    @Override
    protected String getFallback() {
        return null;
    }
}