package appafzar.dataservice.realm;


import android.content.Context;

import androidx.annotation.NonNull;

import appafzar.dataservice.helper.Log;
import appafzar.dataservice.web.model.BaseModel;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 *
 * @param <T>
 */
public abstract class BaseRealmModel<T extends RealmObject> extends BaseModel<T> {

    protected Realm realm;
    private Class<T> typeParameterClass;

    public BaseRealmModel(Context context, Realm realm, Class<T> typeParameterClass) {
        super(context, typeParameterClass);
        this.typeParameterClass = typeParameterClass;
        this.realm = realm;
    }

    public T readLocal(int id) {
        return realm.where(typeParameterClass).equalTo("id", id).findFirst();
    }

    public RealmResults<T> readAllLocal() {
        return realm.where(typeParameterClass).findAll();
    }

    public void createOrUpdateLocal(final String jsonString) {
        if (isNullOrEmpty(jsonString)) return;
        realm.executeTransactionAsync(realm -> {
            realm.createOrUpdateObjectFromJson(typeParameterClass, jsonString);
            Log.i("local " + typeParameterClass.getSimpleName() + " is updated Successfully!");
        });
    }

    public void createOrUpdateAllLocal(final String jsonArrayString) {
        if (isNullOrEmpty(jsonArrayString)) return;
        realm.executeTransactionAsync(realm -> {
            realm.createOrUpdateAllFromJson(typeParameterClass, jsonArrayString);
            Log.i("local " + typeParameterClass.getSimpleName() + " list is updated Successfully!");
        });
    }

    public void replaceAllLocal(final String jsonArrayString) {
        deleteAllLocal();
        if (isNullOrEmpty(jsonArrayString)) return;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.createAllFromJson(typeParameterClass, jsonArrayString);
                Log.i("local " + typeParameterClass.getSimpleName() + " list is replaced Successfully!");
            }
        });
    }

    public void deleteAllLocal() {
        realm.executeTransactionAsync(realm -> realm.delete(typeParameterClass));
    }

    public void deleteLocal(final int id) {
        realm.executeTransactionAsync(realm -> {
            realm.where(typeParameterClass).equalTo("id", id).findFirst().deleteFromRealm();
            Log.i(typeParameterClass.getSimpleName() + " with id " + id + " deleted from realm.");
        });
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.length() == 0);
    }


}
