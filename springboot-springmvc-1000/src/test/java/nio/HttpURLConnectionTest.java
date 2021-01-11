package nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionTest {

    static final String url0 = "http://localhost:1000/simple/test1";
    static final int MAX_N = 1;

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
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            URL url = new URL(url0);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100000); // ms
            connection.setReadTimeout(100000); // ms
            connection.connect();
            System.out.println(Thread.currentThread().getName() + " respcode: " + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                System.out.println(Thread.currentThread().getName() + " result: " + sb.toString());
            }
        } finally {
            if (null != br)
                br.close();
            if (null != is)
                is.close();
            if (connection != null)
                connection.disconnect();
            System.out.println(Thread.currentThread().getName() + " end");
        }
    }
}
