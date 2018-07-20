package com.example.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandWithStubbedFallback extends HystrixCommand<CommandWithStubbedFallback.UserAccount> {

    private final int customerId;
    private final String countryCodeFromGeoLookup;


//    回退：存根化
//    一个存根回退典型的被用于包含多个字段的一个组合对象被返回时。它们其中的一部分能被其它请求状态来决定。当其它字段被设置为默认值。
//    能使用的这些存根值得示例有：
//    cookies
//            请求参数和头
//    从先于目前失败请求的以前的服务请求结果
//    存根值能被静态的从请求范围内取到，但是典型的它建议在command初始化时注入使用

    /**
     * @param customerId
     *            The customerID to retrieve UserAccount for
     * @param countryCodeFromGeoLookup
     *            The default country code from the HTTP request geo code lookup used for fallback.
     */
    protected CommandWithStubbedFallback(int customerId, String countryCodeFromGeoLookup) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.customerId = customerId;
        this.countryCodeFromGeoLookup = countryCodeFromGeoLookup;
    }

    @Override
    protected UserAccount run() {
        // fetch UserAccount from remote service
        //        return UserAccountClient.getAccount(customerId);
        throw new RuntimeException("forcing failure for example");
    }

    @Override
    protected UserAccount getFallback() {
        /**
         * Return stubbed fallback with some static defaults, placeholders,
         * and an injected value 'countryCodeFromGeoLookup' that we'll use
         * instead of what we would have retrieved from the remote service.
         */
        return new UserAccount(customerId, "Unknown Name",
                countryCodeFromGeoLookup, true, true, false);
    }

    public static class UserAccount {
        private final int customerId;
        private final String name;
        private final String countryCode;
        private final boolean isFeatureXPermitted;
        private final boolean isFeatureYPermitted;
        private final boolean isFeatureZPermitted;

        UserAccount(int customerId, String name, String countryCode,
                    boolean isFeatureXPermitted,
                    boolean isFeatureYPermitted,
                    boolean isFeatureZPermitted) {
            this.customerId = customerId;
            this.name = name;
            this.countryCode = countryCode;
            this.isFeatureXPermitted = isFeatureXPermitted;
            this.isFeatureYPermitted = isFeatureYPermitted;
            this.isFeatureZPermitted = isFeatureZPermitted;
        }
    }
}