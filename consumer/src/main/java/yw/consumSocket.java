package yw;

import yw.model.Greeting;
import yw.socket.SocketClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class consumSocket extends  Thread {
    public static AtomicInteger callCount = new AtomicInteger(0);
    public static AtomicInteger successCallCount = new AtomicInteger(0);
    public static AtomicInteger finishedCount = new AtomicInteger(0);
    public static ConcurrentMap<Integer, Long> intervals = new ConcurrentHashMap();

    public void run() {
        Long time = System.currentTimeMillis();
        callSocket(time.toString());
    }

    private void callSocket(String name) {
        try {
            callCount.incrementAndGet();
            SocketClient sc = new SocketClient("10.153.218.121", 2343);
//            SocketClient sc = new SocketClient("localhost", 2343);
            long start = System.currentTimeMillis();
            Greeting g = sc.greeting(name);
            long offset = System.currentTimeMillis() - start;

            if(g == null) {
                System.out.println("callSocket Failed to get response");
            } else {
//                System.out.println("callSocket id: " + g.getId() + " content: " + g.getContent() + " offset: " + offset);
                successCallCount.incrementAndGet();
                intervals.put(successCallCount.get(), offset);
            }
        } finally {
            finishedCount.incrementAndGet();
        }
    }
}
