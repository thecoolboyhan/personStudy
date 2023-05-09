package nettyInAc.part1.callbacks1;

public class MyFetcher implements Fetcher{

    final Data data;

    public MyFetcher(Data data) {
        this.data = data;
    }


    @Override
    public void fetchData(FetcherCallback callback) {
        try {
            callback.onData(data);
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
