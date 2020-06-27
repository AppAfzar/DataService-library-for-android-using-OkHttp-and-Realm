package appafzar.dataservice.web.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import appafzar.dataservice.helper.MySingleton;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
@SuppressLint("StaticFieldLeak")
class VallyObjectRequestAsync extends AsyncTask<String, Void, Void> {

    private OnPostExecuteListener onPostExecuteListener;
    private JSONObject jsonRequest;
    private Context context;

    public VallyObjectRequestAsync(Context context, JSONObject jsonRequest, OnPostExecuteListener onPostExecuteListener) {
        this.onPostExecuteListener = onPostExecuteListener;
        this.jsonRequest = jsonRequest;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        JsonObjectRequest request = new JsonObjectRequest
                (Integer.valueOf(params[0]), params[1], jsonRequest,
                        new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                onPostExecuteListener.result(String.valueOf(response));
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onPostExecuteListener.result(null);
                    }

                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(request);

        return null;
    }

    public interface OnPostExecuteListener {
        void result(String response);
    }
}
