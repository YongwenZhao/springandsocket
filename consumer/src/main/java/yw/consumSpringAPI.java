package yw;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import yw.model.Greeting;

public class consumSpringAPI extends  Thread {
    public static AtomicInteger callCount = new AtomicInteger(0);
    public static AtomicInteger successCallCount = new AtomicInteger(0);
    public static AtomicInteger finishedCount = new AtomicInteger(0);
    public static ConcurrentMap<Integer, Long> intervals = new ConcurrentHashMap();

    public void run() {
        Long time = System.currentTimeMillis();
        callGreetingAPI(time.toString());
    }

    private void callGreetingAPI(String name) {
        callCount.incrementAndGet();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet("http://10.153.218.120:2345/api/greeting?name=" + name);
//            HttpGet get = new HttpGet("http://localhost:2345/api/greeting?name=" + name);
            long start = System.currentTimeMillis();
            CloseableHttpResponse response = httpClient.execute(get);
            Greeting g = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), Greeting.class);
            long offset = System.currentTimeMillis() - start;
//            System.out.println("Spring Boot id: " + g.getId() + " content: " + g.getContent() + " offset: " + offset);
            if(g != null) {
                successCallCount.incrementAndGet();
                intervals.put(successCallCount.get(), offset);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            finishedCount.incrementAndGet();
        }
    }
}
