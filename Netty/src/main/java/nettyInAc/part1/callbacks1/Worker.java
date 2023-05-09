package nettyInAc.part1.callbacks1;

public class Worker {
    public void doWork(){
        Fetcher fetcher = new MyFetcher(new Data(1, 1));
        fetcher.fetchData(new FetcherCallback() {
            @Override
            public void onData(Data data) throws Exception {
                System.out.println("Data received :"+data);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("An error accour:"+ throwable.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.doWork();
    }
}
