package appafzar.dataservice.web.handler;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseJsonObject extends ResponseHandler {

    private ResponseInterface responseInterface;

    public ResponseJsonObject(ResponseInterface responseInterface) {
        this.responseInterface = responseInterface;
    }

    //region Response
    @Override
    public void onStartRequest() {
        responseInterface.onStartRequest();
    }

    @Override
    public void onTimeOut(Exception e) {
        responseInterface.onTimeOut(e);
    }

    @Override
    public void onForbiddenAccess(int code) {
        responseInterface.onForbiddenAccess(code);
    }

    @Override
    public void onUnauthorized(Exception e) {
        responseInterface.onUnauthorized(e);
    }

    @Override
    public void onFailure(Exception e) {
        responseInterface.onFailure(e);
    }

    @Override
    public void onFinishRequest() {
        responseInterface.onFinishRequest();
    }

    public void onSuccess(JSONObject response) {

    }
    //endregion

    //region Invoke
    private void invokeOnSuccess(JSONObject response) {
        onSuccess(response);
    }

    public void invokeOnSuccess(String responseString) {
        super.invokeOnSuccess(responseString);
        try {
            invokeOnSuccess(new JSONObject(responseString));
        } catch (JSONException ex) {
            invokeOnFailure(ex);
        }
    }
    //endregion
}
