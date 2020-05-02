package com.bandoh.banpost.Helpers;

import com.bandoh.banpost.Helpers.enums.ReqType;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.*;

public class MainControllerHelper {

    private ReqType reqType;

    public String[] reqHandler(String url, String postBody, String header) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(postBody, JSON);
        Request request = null;
        // String d = "";
        String a = "";
        Headers.Builder h = null;
        Headers hbuild = null;
        System.out.println(header);
        if (!header.equals("{")) {
            a = header.substring(1, header.length() - 1);
            String[] all = a.split(";");
            h = new Headers.Builder();
            for (String al : all) {
                String f = al.split(",")[0];
                String s = al.split(",")[1];
                System.out.println(f + " : " + s);
                h.add(f, s);
            }
            hbuild = h.build();
        }
     
        switch (this.reqType) {
            case GET: {
                if (!header.equals("{"))
                request = new Request.Builder().url(url).headers(hbuild).build();
                else request = new Request.Builder().url(url).build();
                break;
            }
            case POST: {
                if (!header.equals("{"))
                request = new Request.Builder().url(url).headers(hbuild).post(body).build();
                else request = new Request.Builder().url(url).post(body).build();
                break;
            }
            default:
                break;
        }
        Response response = okHttpClient.newCall(request).execute();
        System.out.println(request.headers());
        String[] res = { response.body().string(), response.headers().get("content-type").split(";")[0] };
        response.close();
        return res;
    }

    public void setReqType(ReqType d) {
        reqType = d;
    }

    public ReqType getReqType() {
        return reqType;
    }

}
// jsonplaceholder.typicode.com/todos