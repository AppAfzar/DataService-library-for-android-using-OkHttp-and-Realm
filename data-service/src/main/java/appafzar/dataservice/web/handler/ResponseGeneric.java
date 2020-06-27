package appafzar.dataservice.web.handler;

import appafzar.dataservice.helper.Convert;
import okhttp3.Response;


public class ResponseGeneric<T> extends ResponseHandler {

    //region Property
    private Class<T> typeParameterClass;
    private ResponseInterface responseInterface;
    private T genericResponse = null;

    public ResponseGeneric(Class<T> typeParameterClass, ResponseInterface responseInterface) {
        this.typeParameterClass = typeParameterClass;
        this.responseInterface = responseInterface;
    }
    //endregion

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

    public void onSuccess(String jsonObject, T t) {

    }
    //endregion

    //region Invoke
    private void invokeOnSuccess(String jasonObject, T t) {
        onSuccess(jasonObject, t);
    }

    @Override
    public void invokeOnSuccess(String jasonObject) {
        super.invokeOnSuccess(jasonObject);
        invokeOnSuccess(jasonObject, getGenericResponse());
    }


    public T getGenericResponse() {
        return genericResponse;
    }

    @Override
    public void convertResponse(Response response) {
        try {
            super.convertResponse(response);
            String stringResponse = super.getStringResponse();

            if (stringResponse == null || stringResponse.isEmpty())
                throw new Exception(ERROR_NULL_OR_EMPTY);

            genericResponse = Convert.jsonObjectToEntity(stringResponse, typeParameterClass);
        } catch (Exception ex) {
            sendFailureMessage(ex);
        }
    }
    //endregion
}
