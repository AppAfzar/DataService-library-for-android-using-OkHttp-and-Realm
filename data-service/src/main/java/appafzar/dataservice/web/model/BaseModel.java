package appafzar.dataservice.web.model;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import appafzar.dataservice.web.handler.ResponseGeneric;
import appafzar.dataservice.web.handler.ResponseGenericList;
import appafzar.dataservice.web.handler.ResponseJsonObject;
import appafzar.dataservice.web.handler.ResponsePagination;

/**
 * Accessing to any data for reading and writing is available throw this class.
 * android shared preferences, local database (realm) and remote server database.
 * <p>
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 *
 * @param <T> Entity class
 */
public class BaseModel<T> {

    //region Property
    //Important: Set isConnected in connectivity change status
    //recommendation: create a connectivity change listener in base Activity to set this parameter
    public static boolean isConnected = true;

    private final Class<T> typeParameterClass;
    // This controller use to send and receive data from and to a web server.
    public WebController<T> web;
    // This model is used for write and read data in preference locally.
    public PreferenceModel<T> preferenceModel;
    // A context always required for any stuff to do.
    private Context context;

    /**
     * @param typeParameterClass type of entity class.
     * @param context            app or activity context.
     * @see BaseModel :
     */
    public BaseModel(Context context, Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        this.context = context;
        web = new WebController<>(context);
        web.setControllerName(typeParameterClass.getSimpleName().toLowerCase());
//        local = new MyRealmModel<>(typeParameterClass);
        preferenceModel = new PreferenceModel<>(context, typeParameterClass);
    }

    protected Context getContext() {
        return context;
    }
    //endregion

    //region Create

    public void create(T t, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.create(t, new ResponseGeneric<T>(typeParameterClass, dataInterface) {

                @Override
                public void onSuccess(String jsonString, T t) {
                    dataInterface.onDataResponse(t);
                    dataInterface.onDataResponse(jsonString);
                }
            });
        } else sendConnectionErrorMessage(dataInterface);
    }

    public void create(T t, HashMap<String, Object> params, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.create(t, params, new ResponseGeneric<T>(typeParameterClass, dataInterface) {

                @Override
                public void onSuccess(String jsonString, T t) {
                    dataInterface.onDataResponse(t);
                    dataInterface.onDataResponse(jsonString);
                }
            });
        } else sendConnectionErrorMessage(dataInterface);
    }
    //endregion

    //region Read
    public void read(int id, DataInterface<T> dataInterface) {
//        dataInterface.onDataResponse(local.readById(id));
        if (isConnected) {
            web.read(id, new ResponseGeneric<T>(typeParameterClass, dataInterface) {

                @Override
                public void onSuccess(String jsonString, T t) {
                    dataInterface.onDataResponse(t);
                    dataInterface.onDataResponse(jsonString);
                }
            });
        } else sendConnectionErrorMessage(dataInterface);
    }

    public void readAll(DataInterface<T> dataInterface) {
        readAll(null, dataInterface);
    }

    public void readAll(HashMap<String, Object> extraParams, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.readAll(extraParams, new ResponseGenericList<T>(typeParameterClass, dataInterface) {
                @Override
                public void onSuccess(String jsonArrayString, List<T> lstT) {
                    dataInterface.onDataListResponse(lstT, 1, 1);
                    dataInterface.onDataListResponse(jsonArrayString);
//                    local.sync(jsonArrayString);
                }
            });
        } else sendConnectionErrorMessage(dataInterface);
    }

    public void readAllPaginate(int page, int perPage, DataInterface<T> dataInterface) {
        readAllPaginate(page, perPage, null, dataInterface);
    }

    public void readAllPaginate(int page, int perPage, HashMap<String, Object> extraParams, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.readAllPaginate(page, perPage, extraParams, new ResponsePagination<T>(typeParameterClass, dataInterface) {
                @Override
                public void onSuccess(String jsonArrayString, List<T> lstT,
                                      int pageNo, int pageCount, int itemsPerPage) {
                    dataInterface.onDataListResponse(lstT, pageNo, pageCount);
                    dataInterface.onDataListResponse(jsonArrayString);
                }
            });
        } else sendConnectionErrorMessage(dataInterface);
    }
    //endregion

    //region Update


    /**
     * @param t: data object
     */
    public void update(T t, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.update(t, new ResponseGeneric<T>(typeParameterClass, dataInterface) {

                @Override
                public void onSuccess(String jsonString, T t) {
                    dataInterface.onDataResponse(t);
                    dataInterface.onDataResponse(jsonString);
                }

            });
        } else sendConnectionErrorMessage(dataInterface);
    }

    public void update(T t, HashMap<String, Object> params, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.update(t, params, new ResponseGeneric<T>(typeParameterClass, dataInterface) {

                @Override
                public void onSuccess(String jsonString, T t) {
                    dataInterface.onDataResponse(t);
                    dataInterface.onDataResponse(jsonString);
                }

            });
        } else sendConnectionErrorMessage(dataInterface);
    }

    //endregion

    //region Delete

    /**
     * delete a row from table by id
     *
     * @param t: entity that should be deleted.
     */
    public void delete(T t, DataInterface<T> dataInterface) {
        if (isConnected) {
            web.delete(t, new ResponseJsonObject(dataInterface) {
                @Override
                public void onSuccess(JSONObject response) {
                    dataInterface.onDeleteResponse(true);
                    //todo hey you! what is your plan for updating local data on delete?
                }
            });
        } else sendConnectionErrorMessage(dataInterface);
    }
    //endregion

    //region Methods
    public void sendConnectionErrorMessage(DataInterface dataInterface) {
        dataInterface.onConnectionError(new Exception("No Internet Connection!"));
        dataInterface.onFinishRequest();
    }
    //endregion

}
