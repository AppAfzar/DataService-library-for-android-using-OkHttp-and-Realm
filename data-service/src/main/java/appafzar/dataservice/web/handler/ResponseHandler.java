package appafzar.dataservice.web.handler;

import android.annotation.SuppressLint;

import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Response;

public abstract class ResponseHandler extends BaseResponseHandler {

    //region Property
    //A variable for keeping converted raw response
    //in order to use in UI thread later.
    private String stringResponse = null;

    public ResponseHandler() {
        super();
    }
    //endregion

    //region Invoke
    @Override
    public void invokeOnSuccess(Call call, Response response) {
        super.invokeOnSuccess(call, response);
        invokeOnSuccess(getStringResponse());
    }

    public void invokeOnSuccess(String response) {
        onSuccess(response);
    }

    public void onSuccess(String response) {

    }

    @Override
    public void invokeOnFailure(Exception e) {
        super.invokeOnFailure(e);
    }

    public void invokeOnFinish() {
        try {
            onFinishRequest();
        } catch (Exception ex) {
            invokeOnFailure(ex);
        }
    }
    //endregion

    //region Convert response
    public void invokeOnStart() {
        try {
            onStartRequest();
        } catch (Exception ex) {
            invokeOnFailure(ex);
        }
    }

    /**
     * Get the response which is converted in a background thread
     * before by calling {@link #convertResponse(Response)} .
     *
     * @return string converted raw response
     */
    public final String getStringResponse() {
        return stringResponse;
    }

    //endregion
    @Override
    @SuppressLint("NewApi")
    public void convertResponse(Response response) {
        try {
            super.convertResponse(response);
            stringResponse = new String(getByteResponse(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            sendFailureMessage(ex);
        }
    }
}
