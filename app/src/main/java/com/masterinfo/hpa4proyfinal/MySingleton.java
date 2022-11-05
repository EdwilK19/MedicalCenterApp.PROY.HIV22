package com.masterinfo.hpa4proyfinal;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton instancia;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MySingleton(Context contexto) {
        ctx = contexto;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context contexto) {
        if (instancia == null) {
            instancia = new MySingleton(contexto);
        }
        return instancia;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() es clave, evita que se filtre el
            // Activity or BroadcastReceiver si alguien pasa uno.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
