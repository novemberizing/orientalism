package net.novemberizing.orientalism;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class OrientalismApplicationVolley {

    public static void onDefaultError(VolleyError error) {
        if(error != null) {
            Log.e(Tag, error.toString());
        }
    }

    private static final String Tag = "OrientalismApplicationVolley";
    private static OrientalismApplicationVolley instance = null;

    public static void gen(Context context) {
        synchronized (OrientalismApplicationVolley.class) {
            if(instance == null) {
                instance = new OrientalismApplicationVolley(context);
            }
        }
    }

    public static Request<String> str(String url, Response.Listener<String> success, Response.ErrorListener fail) {
        return instance.queue.add(new StringRequest(url, success, fail));
    }

    public static <T> Request<T> json(String url, Class<T> c, Response.Listener<T> success, Response.ErrorListener fail) {
        return instance.queue.add(new Request<T>(Request.Method.GET, url, fail) {
            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse response) {
                try {
                    Gson gson = OrientalismApplicationGson.get();
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(gson.fromJson(json, c), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(T response) {
                success.onResponse(response);
            }
        });
    }

    private Context context;
    private RequestQueue queue;

    public OrientalismApplicationVolley(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }
}
