package com.hankyang.java8.samples.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Date: 2018/5/31
 * Author: HankYang
 */
public class Atomic1 {
    private static final int NUM_INCREMENTS = 1000;
    private static AtomicInteger atomicInteger  = new AtomicInteger(0);

    public static void main(String[] args) {
        testUpdate();

        testAccumlate();

        testIncrement();
    }

    private static void testUpdate(){
        atomicInteger.set(0);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(0,NUM_INCREMENTS)
                .forEach(i->{
                    Runnable task = ()->
                            atomicInteger.updateAndGet(n -> n + 2);
                    executorService.submit(task);
                });
        ConcurrentUtils.stop(executorService);
        System.out.format("Update: %d\n",atomicInteger.get());
    }

    private static void testAccumlate(){
        atomicInteger.set(0);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(0,NUM_INCREMENTS)
                .forEach(i-> {
                    Runnable task = () ->
                        atomicInteger.accumulateAndGet(i,(n,m) -> n + m);
                    executorService.submit(task);
                });
        ConcurrentUtils.stop(executorService);

        System.out.format("Acccumlate: %d\n",atomicInteger.get());

    }

    private static void testIncrement() {
        atomicInteger.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(atomicInteger::incrementAndGet));

        ConcurrentUtils.stop(executor);

        System.out.format("Increment: Expected=%d; Is=%d\n", NUM_INCREMENTS, atomicInteger.get());
    }

}
