package appafzar.dataservice.web.model;

import java.util.List;

import appafzar.dataservice.web.handler.ResponseInterface;

public interface DataInterface<T> extends ResponseInterface {

    void onDataResponse(T t);

    void onDataResponse(String jsonResponse);

    void onDataListResponse(List<T> lstT, int pageNo, int pageCount);

    void onDataListResponse(String jsonResponse);

    void onDeleteResponse(boolean done);

    void onConnectionError(Exception e);

}
