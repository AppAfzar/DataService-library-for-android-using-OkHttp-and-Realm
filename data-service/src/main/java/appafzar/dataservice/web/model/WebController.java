package appafzar.dataservice.web.model;

import android.content.Context;

import java.util.HashMap;

import appafzar.dataservice.web.handler.ResponseGeneric;
import appafzar.dataservice.web.handler.ResponseGenericList;
import appafzar.dataservice.web.handler.ResponseHandler;
import appafzar.dataservice.web.handler.ResponseJsonObject;
import appafzar.dataservice.web.handler.ResponsePagination;

import static appafzar.dataservice.Config.ACTION_CREATE;
import static appafzar.dataservice.Config.ACTION_DELETE;
import static appafzar.dataservice.Config.ACTION_READ;
import static appafzar.dataservice.Config.ACTION_READ_ALL;
import static appafzar.dataservice.Config.ACTION_UPDATE;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 *
 * @param <T>
 */
public class WebController<T> extends BaseWebController<T> {

    public WebController(Context context) {
        super(context);
    }

    //region Create
    public void create(T t, ResponseHandler responseHandler) {
        this.post(t, null, ACTION_CREATE, responseHandler);

    }

    public void create(T t, HashMap<String, Object> params, ResponseHandler responseHandler) {
        this.post(t, params, ACTION_CREATE, responseHandler);

    }
    //endregion

    //region Read
    public void read(int id, ResponseGeneric<T> responseHandler) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("id", id);
        this.get(param, ACTION_READ, responseHandler);
    }

    public void readAll(HashMap<String, Object> extraParam, ResponseGenericList<T> responseHandler) {
        this.post(null, extraParam, ACTION_READ_ALL, responseHandler);
    }

    public void readAllPaginate(int page, int perPage, ResponsePagination<T> response) {
        this.readAllPaginate(page, perPage, null, response);
    }

    public void readAllPaginate(int page, int perPage, HashMap<String, Object> extraParam, ResponsePagination<T> response) {
        if (extraParam == null) extraParam = new HashMap<>();
        extraParam.put("page", page);
        extraParam.put("per_page", perPage);
        post(null, extraParam, ACTION_READ_ALL, response);
    }
    //endregion

    //region Update

    /**
     * update one row of table according to object t
     *
     * @param t:               data object
     * @param responseHandler: StringResponseHandler.
     */
    public void update(T t, ResponseGeneric<T> responseHandler) {
        this.post(t, null, ACTION_UPDATE, responseHandler);
    }

    public void update(T t, HashMap<String, Object> params, ResponseGeneric<T> responseHandler) {
        this.post(t, params, ACTION_UPDATE, responseHandler);
    }
    //endregion

    //region Delete

    /**
     * delete a row from table by id
     *
     * @param t:               entity that should be deleted.
     * @param responseHandler: StringResponseHandler
     */
    public void delete(T t, ResponseJsonObject responseHandler) {
        this.post(t, null, ACTION_DELETE, responseHandler);
    }
    //endregion
}
