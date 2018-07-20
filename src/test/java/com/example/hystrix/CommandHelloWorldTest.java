package com.example.hystrix;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CommandHelloWorldTest {




    @Test
    public void testSynchronous() {
        //同步执行
        String s = new CommandHelloWorld("World").execute();
        assertEquals("Hello World!", new CommandHelloWorld("World").execute());
        assertEquals("Hello Bob!", new CommandHelloWorld("Bob").execute());

//        下面的代码功能是相同的
//
//        String s1 = new CommandHelloWorld("World").execute();
//        String s2 = new CommandHelloWorld("World").queue().get();
    }

    @Test
    public void testAsynchronous1() throws Exception {
        //异步执行
        Future<String> fs = new CommandHelloWorld("World").queue();
        String s = fs.get();

        assertEquals("Hello World!", new CommandHelloWorld("World").queue().get());
        assertEquals("Hello Bob!", new CommandHelloWorld("Bob").queue().get());
    }

    @Test
    public void testAsynchronous2() throws Exception {

        Future<String> fWorld = new CommandHelloWorld("World").queue();
        Future<String> fBob = new CommandHelloWorld("Bob").queue();

        assertEquals("Hello World!", fWorld.get());
        assertEquals("Hello Bob!", fBob.get());
    }


    @Test
    public void testObservable() throws Exception {
//        响应式执行（异步回调）通过使用observe() 执行
//
//        Observable<String> fs = new CommandHelloWorld("World").observe();
//        返回值可以通过订阅Observable获得
//
//        fs.subscribe(new Action1<String>() {
//
//            @Override
//            public void call(String s) {
//                // value emitted here
//            }
//
//        });
//
        Observable<String> fWorld = new CommandHelloWorld("World").observe();
        Observable<String> fBob = new CommandHelloWorld("Bob").observe();

        // blocking
        assertEquals("Hello World!", fWorld.toBlocking().single());
        assertEquals("Hello Bob!", fBob.toBlocking().single());

        // non-blocking
        // - this is a verbose anonymous inner-class approach and doesn't do assertions
        fWorld.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                // nothing needed here
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("onNext: " + v);
            }

        });

        // non-blocking
        // - also verbose anonymous inner-class
        // - ignore errors and onCompleted signal
        fBob.subscribe(new Action1<String>() {

            @Override
            public void call(String v) {
                System.out.println("onNext: " + v);
            }

        });
    }




//
//    无声失败
//
//    无声的失败等同于返回一个空的响应或者删除功能,它通过返回null，空的map对象，空的list或者其他类似的响应实现。
//    通常通过HystrixCommand实例中的getFallback() 方法实现

    @Test
    public void testSuccess() {
        assertEquals("success", new CommandThatFailsSilently(false).execute());
    }

    @Test
    public void testFailure() {
        try {
            assertEquals(null, new CommandThatFailsSilently(true).execute());
        } catch (HystrixRuntimeException e) {
            fail("we should not get an exception as we fail silently with a fallback");
        }
    }




    @Test
    public void test() {
        CommandWithStubbedFallback command = new CommandWithStubbedFallback(1234, "ca");
        CommandWithStubbedFallback.UserAccount account = command.execute();
        assertTrue(command.isFailedExecution());
        assertTrue(command.isResponseFromFallback());
//        assertEquals(1234, account.customerId);
//        assertEquals("ca", account.countryCode);
//        assertEquals(true, account.isFeatureXPermitted);
//        assertEquals(true, account.isFeatureYPermitted);
//        assertEquals(false, account.isFeatureZPermitted);
    }
}
