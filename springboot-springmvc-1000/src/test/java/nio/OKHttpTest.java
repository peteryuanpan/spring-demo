package nio;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OKHttpTest {

    static final String url0 = "http://localhost:1000/simple/test1";
    static final int MAX_N = 1;
    static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                runTask();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread[] ths = new Thread[MAX_N];
        for (int i = 0; i < MAX_N; i ++) {
            ths[i] = new Thread(t, "Thread-" + i);
            ths[i].start();
        }

        for (int i = 0; i < MAX_N; i ++) {
            ths[i].join();
        }
    }

    static void runTask() throws IOException {
        Request.Builder builder = new Request.Builder();
        builder.url(url0);
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        System.out.println(response.body().string());
    }
}
