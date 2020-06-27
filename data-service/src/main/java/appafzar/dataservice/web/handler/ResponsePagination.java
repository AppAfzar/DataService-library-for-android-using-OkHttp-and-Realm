package appafzar.dataservice.web.handler;


import org.json.JSONObject;

import java.util.List;

import appafzar.dataservice.helper.Convert;
import appafzar.dataservice.helper.Tools;
import okhttp3.Response;

import static appafzar.dataservice.Config.RESPONSE_KEY_DATA;
import static appafzar.dataservice.Config.RESPONSE_PAGE_NUMBER;
import static appafzar.dataservice.Config.RESPONSE_PAGE_SIZE;
import static appafzar.dataservice.Config.RESPONSE_PAGE_TOTAL_COUNT;

public class ResponsePagination<T> extends ResponseHandler {

    //region Property
    private final Class<T> typeParameterClass;
    private ResponseInterface responseInterface;
    private JSONObject jsonObjectResponse = null;
    private List<T> genericListResponse = null;
    private String dataJsonArrayString = "[]";


    public ResponsePagination(Class<T> typeParameterClass, ResponseInterface responseInterface) {
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

    public void onSuccess(String jsonArrayString, List<T> lstT, int pageNo, int pageCount, int itemsPerPage) {

    }
    //endregion

    //region Invoke
    private void invokeOnSuccess(String jsonArrayString, List<T> lstT, int pageNo, int pageCount, int itemsPerPage) {
        try {
            onSuccess(jsonArrayString, lstT, pageNo, pageCount, itemsPerPage);
        } catch (Exception ex) {
            invokeOnFailure(ex);
        }
    }

    public void invokeOnSuccess(String responseString) {
        try {
            super.invokeOnSuccess(responseString);
            if (jsonObjectResponse == null) convertResponse(responseString);

            int pageNo = jsonObjectResponse.getInt(RESPONSE_PAGE_NUMBER);
            int itemsPerPage = jsonObjectResponse.getInt(RESPONSE_PAGE_SIZE);
            int pageCount = jsonObjectResponse.getInt(RESPONSE_PAGE_TOTAL_COUNT);
            List<T> lstT = genericListResponse;
            invokeOnSuccess(dataJsonArrayString, lstT, pageNo, pageCount, itemsPerPage);
        } catch (Exception ex) {
            invokeOnFailure(ex);
        }
    }

    @Override
    public void convertResponse(Response response) {
        try {
            super.convertResponse(response);
            String stringResponse = super.getStringResponse();

            if (Tools.isNullOrEmpty(stringResponse))
                throw new Exception(ERROR_NULL_OR_EMPTY);

            jsonObjectResponse = new JSONObject(stringResponse);
            dataJsonArrayString = jsonObjectResponse.getString(RESPONSE_KEY_DATA);
            genericListResponse = Convert.jsonArrayToEntityList(dataJsonArrayString, typeParameterClass);
        } catch (Exception ex) {
            sendFailureMessage(ex);
        }
    }

    public void convertResponse(String stringResponse) {
        try {
            if (Tools.isNullOrEmpty(stringResponse))
                throw new Exception(ERROR_NULL_OR_EMPTY);

            jsonObjectResponse = new JSONObject(stringResponse);
            dataJsonArrayString = jsonObjectResponse.getString(RESPONSE_KEY_DATA);
            genericListResponse = Convert.jsonArrayToEntityList(dataJsonArrayString, typeParameterClass);
        } catch (Exception ex) {
            sendFailureMessage(ex);
        }
    }
    //endregion
}