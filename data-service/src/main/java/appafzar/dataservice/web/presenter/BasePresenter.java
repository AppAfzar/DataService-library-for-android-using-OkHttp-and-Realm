package appafzar.dataservice.web.presenter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import appafzar.dataservice.web.model.BaseModel;
import appafzar.dataservice.web.model.DataInterface;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 *
 * @param <T>
 */
public abstract class BasePresenter<T> implements DataInterface<T> {

    //region Property
    private DataInterface<T> view;
    private BaseModel<T> model;

    public BasePresenter(Context context, DataInterface<T> view) {
        this.model = setDataModel(context);
        this.view = view;
    }

    protected abstract BaseModel<T> setDataModel(Context context);
    //endregion

    //region Read
    public void present(int id) {
        model.read(id, this);
    }

    public void presentAll() {
        model.readAll(null, this);
    }

    public void presentAll(HashMap<String, Object> extraParams) {
        model.readAll(extraParams, this);
    }

    public void presentAllPaginate(int page, int perPage) {
        model.readAllPaginate(page, perPage, null, this);
    }

    public void presentAllPaginate(HashMap<String, Object> extraParams, int page, int perPage) {
        model.readAllPaginate(page, perPage, extraParams, this);
    }
    //endregion

    //region Create
    public void create(T t) {
        model.create(t, this);
    }

    public void create(T t, HashMap<String, Object> params) {
        model.create(t, params, this);
    }
    //endregion

    //region Update
    public void update(T t) {
        model.update(t, null, this);
    }

    /**
     * @param t      data object
     * @param params any extra parameters
     */
    public void update(T t, HashMap<String, Object> params) {
        model.update(t, params, this);
    }

    //endregion

    //region Delete

    /**
     * delete a row from table by id
     *
     * @param t: entity that should be deleted.
     */
    protected void delete(T t) {
        model.delete(t, this);
    }
    //endregion

    //region Data Interface
    @Override
    public void onDataResponse(T t) {
        view.onDataResponse(t);
    }

    @Override
    public void onDataListResponse(List<T> lstT, int pageNo, int pageCount) {
        view.onDataListResponse(lstT, pageNo, pageCount);
    }

    @Override
    public void onDeleteResponse(boolean done) {
        view.onDeleteResponse(done);
    }

    @Override
    public void onConnectionError(Exception e) {
        view.onConnectionError(e);
    }

    @Override
    public void onStartRequest() {
        view.onStartRequest();
    }

    @Override
    public void onTimeOut(Exception e) {
        view.onTimeOut(e);
    }

    @Override
    public void onForbiddenAccess(int code) {
        view.onForbiddenAccess(code);
    }

    @Override
    public void onUnauthorized(Exception e) {
        view.onUnauthorized(e);
    }

    @Override
    public void onFailure(Exception e) {
        view.onFailure(e);
    }

    @Override
    public void onFinishRequest() {
        view.onFinishRequest();
    }

    public abstract void onDataResponse(String jsonResponse);

    public abstract void onDataListResponse(String jsonResponse);

    //endregion Data Interface

}
