package yw;

public class callSocketApplication {
    public static void main(String[] strs) {
        for (int i = 0; i < 1000; i++) {
            consumSocket cs = new consumSocket();
            cs.start();
        }
        while (true) {
            if (consumSocket.finishedCount.get() == consumSocket.callCount.get()) {
                long totalTime = 0;
                for (long l : consumSocket.intervals.values()) {
                    totalTime += l;
                }
                double avgTime = totalTime / consumSocket.successCallCount.get();
                System.out.println("Socket Application " + consumSocket.callCount + "calls " + consumSocket.successCallCount + "success calls used " + totalTime + "ms avg time is " + avgTime + "ms");
                break;
            }
        }
    }
}
