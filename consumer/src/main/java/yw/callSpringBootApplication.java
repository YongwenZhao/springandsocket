package yw;

public class callSpringBootApplication {
    public static void main(String[] strs) {
        for (int i=0; i<1000; i++) {
            consumSpringAPI csa = new consumSpringAPI();
            csa.start();
        }
        while(true) {
            if(consumSpringAPI.finishedCount.get() == consumSpringAPI.callCount.get()) {
                long totalTime = 0;
                for(long l : consumSpringAPI.intervals.values()) {
                    totalTime += l;
                }
                double avgTime = totalTime/consumSpringAPI.successCallCount.get();
                System.out.println("Spring boot " + consumSpringAPI.callCount + "calls " + consumSpringAPI.successCallCount + "success calls used " + totalTime + "ms avg time is " + avgTime + "ms");
                break;
            }
        }
    }
}
