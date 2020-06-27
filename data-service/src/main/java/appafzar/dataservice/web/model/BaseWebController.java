package appafzar.dataservice.web.model;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import appafzar.dataservice.Config;
import appafzar.dataservice.helper.Convert;
import appafzar.dataservice.helper.Log;
import appafzar.dataservice.helper.Tools;
import appafzar.dataservice.web.entity.MultiPartFile;
import appafzar.dataservice.web.handler.ResponseHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static appafzar.dataservice.Config.SERVER_FORBIDDEN_ACCESS;
import static appafzar.dataservice.Config.SERVER_UNAUTHORIZED_USER;
import static appafzar.dataservice.Config.call_timeout;
import static appafzar.dataservice.Config.connection_read_timeout;
import static appafzar.dataservice.Config.connection_timeout;
import static appafzar.dataservice.Config.connection_write_timeout;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 *
 * @param <T>
 */
abstract class BaseWebController<T> {

    private String controllerName;
    private Context context;

    public BaseWebController(Context context) {
        this.context = context;
    }

    //region main methods
    public void post(T t, HashMap<String, Object> extraParam, String actionName, ResponseHandler response) {
        JSONObject jsonRequest = null;
        try {
            HashMap<String, Object> params = new HashMap<>();
            if (t != null)
                params.putAll(Convert.entityToHashMap(t));
            if (extraParam != null)
                params.putAll(extraParam);
            jsonRequest = Convert.mapToJsonObject(params);
        } catch (Exception ex) {
            Log.e(ex);
        }
        Log.i(String.valueOf(jsonRequest));
        post(jsonRequest, actionName, response);
    }

    public void post(HashMap<String, Object> extraParam, String actionName, ResponseHandler response) {
        JSONObject jsonRequest = Convert.mapToJsonObject(extraParam);
        Log.i(String.valueOf(jsonRequest));
        post(jsonRequest, actionName, response);
    }

    public void post(JSONObject jsonRequest, String actionName, final ResponseHandler responseHandler) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postRequestOkHttp(String.valueOf(jsonRequest), actionName, responseHandler);
        } else {
            postVallyRequest(jsonRequest, actionName, responseHandler);
        }
    }

    public void get(HashMap<String, Object> params, String actionName, final ResponseHandler responseHandler) {
        //send get request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getRequestOkHttp(params, actionName, responseHandler);
        } else {
            getVallyRequest(params, actionName, responseHandler);
        }
    }
    //endregion main methods

    //region Vally
    private void getVallyRequest(HashMap<String, Object> params, String actionName, final ResponseHandler responseHandler) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(buildWebPath(actionName)).newBuilder();

        if (params != null) {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String value = (pair.getValue() == null ? "" : pair.getValue().toString());
                urlBuilder.addQueryParameter(pair.getKey().toString(), value);
                it.remove();
            }
        }

        vallyRequest(com.android.volley.Request.Method.GET, null, actionName, responseHandler);
    }

    private void postVallyRequest(JSONObject jsonRequest, String actionName, final ResponseHandler responseHandler) {
        vallyRequest(com.android.volley.Request.Method.POST, jsonRequest, actionName, responseHandler);
    }

    private void vallyRequest(int method, JSONObject jsonRequest, String actionName, final ResponseHandler responseHandler) {
        Log.d("\nVally Request method=" + method + " to url: " + buildWebPath(actionName));
        responseHandler.invokeOnStart();
        new VallyObjectRequestAsync(context, jsonRequest, response -> {
            Log.d("\nVally object response: " + response);
            if (!Tools.isNullOrEmpty(response))
                responseHandler.invokeOnSuccess(response);
            else
                responseHandler.invokeOnFailure(new Exception("Vally JsonObject Request failed!"));
            responseHandler.invokeOnFinish();
        }).execute(String.valueOf(method), buildWebPath(actionName));
    }
    //endregion Vally

    //region OkHttp
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void postRequestOkHttp(String jsonString, String actionName, ResponseHandler responseHandler) {
        RequestBody requestBody = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));
        postRequestOkHttp(requestBody, actionName, responseHandler);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void postRequestOkHttp(RequestBody requestBody, String actionName, ResponseHandler responseHandler) {
        String path = buildWebPath(actionName);
        Log.i("\nPost Request to url: " + path);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connection_timeout, TimeUnit.SECONDS)
                .writeTimeout(connection_write_timeout, TimeUnit.SECONDS)
                .readTimeout(connection_read_timeout, TimeUnit.SECONDS)
                .callTimeout(call_timeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(path)
                .post(requestBody)
                .build();
        setCallBack(client.newCall(request), responseHandler);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void postRequestOkHttp(T t, HashMap<String, Object> extraParam, File file, String actionName,
                                   ResponseHandler responseHandler) {
        MultiPartFile multiPartFile = new MultiPartFile("image", file.getName(), "1", file);
        List<MultiPartFile> multiPartFileList = new ArrayList<>();
        multiPartFileList.add(multiPartFile);
        postRequestOkHttp(t, extraParam, multiPartFileList, actionName, responseHandler);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void postRequestOkHttp(T t, HashMap<String, Object> extraParam, List<MultiPartFile> lstFiles, String actionName,
                                   final ResponseHandler responseHandler) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            if (t != null)
                params.putAll(Convert.entityToHashMap(t));
            if (extraParam != null)
                params.putAll(extraParam);

            RequestBody requestBody;
            Iterator paramIterator = params.entrySet().iterator();

            if (lstFiles != null && lstFiles.size() > 0) {
                MultipartBody.Builder builder = new MultipartBody.Builder();

                for (MultiPartFile file : lstFiles) {
                    builder.setType(MultipartBody.FORM)
                            .addFormDataPart(
                                    file.name,
                                    file.filename,
                                    RequestBody.create(file.file, MediaType.parse(file.type))
                            );
                }

                while (paramIterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) paramIterator.next();
                    builder.addFormDataPart(pair.getKey().toString(), pair.getValue().toString());
                    paramIterator.remove();
                }

                requestBody = builder.build();
            } else {
                FormBody.Builder builder = new FormBody.Builder();

                while (paramIterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) paramIterator.next();
                    String value = pair.getValue() == null ? "" : pair.getValue().toString();
                    builder.add(pair.getKey().toString(), value);
                    paramIterator.remove();
                }

                requestBody = builder.build();
            }

            postRequestOkHttp(requestBody, actionName, responseHandler);
        } catch (Exception ex) {
            Log.e(ex);
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void getRequestOkHttp(@Nullable HashMap extraParam, String actionName, ResponseHandler responseHandler) {

        String path = buildWebPath(actionName);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(path).newBuilder();
        if (extraParam != null) {
            Iterator it = extraParam.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String value = (pair.getValue() == null ? "" : pair.getValue().toString());
                urlBuilder.addQueryParameter(pair.getKey().toString(), value);
                it.remove();
            }
        }
        Log.i("\nGet Request to url: " + urlBuilder);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connection_timeout, TimeUnit.SECONDS)
                .writeTimeout(connection_write_timeout, TimeUnit.SECONDS)
                .readTimeout(connection_read_timeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        setCallBack(client.newCall(request), responseHandler);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void setCallBack(Call call, final ResponseHandler responseHandler) {
        responseHandler.sendStartMessage();
        call.enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("OkHttp request Failure! " + e.toString());
                if (e instanceof java.net.SocketTimeoutException)
                    responseHandler.sendTimeOutMessage(e);
                else
                    responseHandler.sendFailureMessage(e);

                responseHandler.sendFinishMessage();
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    responseHandler.sendSuccessMessage(call, response);
                    Log.d(call.request().url().toString() + "\n" + responseHandler.getStringResponse());

                } else if (response.code() == SERVER_UNAUTHORIZED_USER) {
                    Log.e("Unauthorized request! " + response.toString());
                    responseHandler.sendUnauthorizedMessage(new Exception(response.toString()));

                } else if (response.code() == SERVER_FORBIDDEN_ACCESS) {
                    Log.e("Forbidden request! " + response.toString());
                    responseHandler.sendForbiddenAccessMessage(response.code());

                } else {
                    Log.e("Failure! " + response.toString());
                    responseHandler.sendFailureMessage(new Exception(response.toString()));

                }
                responseHandler.sendFinishMessage();
            }
        });
    }
    //endregion OkHttp

    //region helpers
    private String buildWebPath(String actionName) {
        Log.i("Controller name is: " + controllerName);
        return Config.apiBaseUrl +
                (controllerName != null ? controllerName + "/" : "") +
                actionName + "?api_token=" + Config.apiToken;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    //endregion helpers
}
