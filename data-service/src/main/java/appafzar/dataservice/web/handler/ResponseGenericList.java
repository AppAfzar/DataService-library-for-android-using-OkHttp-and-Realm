package appafzar.dataservice.web.handler;


import java.util.List;

import appafzar.dataservice.helper.Convert;
import okhttp3.Response;

public class ResponseGenericList<T> extends ResponseHandler {

    //region Property
    private List<T> genericListResponse = null;
    private Class<T> typeParameterClass;
    private ResponseInterface responseInterface;

    public ResponseGenericList(Class<T> typeParameterClass, ResponseInterface responseInterface) {
        this.typeParameterClass = typeParameterClass;
        this.responseInterface = responseInterface;
    }
    //endregion

    //region response
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

    public void onSuccess(String jsonArray, List<T> lstT) {

    }
    //endregion

    //region Invoke
    public void invokeOnSuccess(String jsonArray, List<T> lstT) {
        try {
            onSuccess(jsonArray, lstT);
        } catch (Exception ex) {
            invokeOnFailure(ex);
        }
    }

    public void invokeOnSuccess(String jsonArray) {
        super.invokeOnSuccess(jsonArray);
        invokeOnSuccess(jsonArray, getGenericListResponse());
    }

    private List<T> getGenericListResponse() {
        return genericListResponse;
    }

    @Override
    public void convertResponse(Response response) {
        try {
            super.convertResponse(response);
            final String stringResponse = super.getStringResponse();

            if (stringResponse == null || stringResponse.isEmpty())
                throw new Exception(ERROR_NULL_OR_EMPTY);

            genericListResponse = Convert.jsonArrayToEntityList(stringResponse, typeParameterClass);
            invokeOnSuccess(stringResponse);

        } catch (Exception ex) {
            sendFailureMessage(ex);
        }
    }
    //endregion
}
