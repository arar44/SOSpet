package org.hackerton1501.lkj.sospet.http;

import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by shlee on 15. 1. 24..
 */
public class HttpRequestTaskextends extends AsyncTask<String, String, String> {
    public final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    public String sendGCMPush(String url, String apiKey, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", "key=" + apiKey)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    protected String doInBackground( String... params ){
        try {
            sendGCMPush(params[0], params[1], params[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onProgressUpdate( Integer ... progress ){}

    protected void onPostExecute( Long result ){}
}
