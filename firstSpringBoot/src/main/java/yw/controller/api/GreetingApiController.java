package yw.controller.api;

import yw.model.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingApiController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private static Lock lock = new ReentrantLock();

    @RequestMapping(method=RequestMethod.GET, path="api/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        try {
            lock.lock();
            for(int i = 0; i < 100; i++) {
                long sum = 0;
                for(int j = 0; j < 10000; j++) {
                    sum += j;
                }
                System.out.println(i + " sum: " + sum);
            }
            return new Greeting(counter.incrementAndGet(), String.format(template, name));
        } finally {
            lock.unlock();
        }
    }
}