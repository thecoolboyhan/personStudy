package nettyInAc.part1.callbacks1;

public interface FetcherCallback {
    void onData(Data data) throws Exception;
    void onError(Throwable throwable);
}
