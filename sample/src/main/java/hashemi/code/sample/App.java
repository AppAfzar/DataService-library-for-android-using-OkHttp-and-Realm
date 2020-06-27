package hashemi.code.sample;

import android.app.Application;

import appafzar.dataservice.Config;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    public static Realm realm;

    /**
     * Sets the initial data in {@link io.realm.Realm}. This transaction will be executed only for
     * the first time when database file is created or while migrating the data when
     * {@link RealmConfiguration.Builder#deleteRealmIfMigrationNeeded()} is set.
     */
    private static Realm.Transaction initialData = realm -> {
        //Sample code to create 50 fake data for post:
        // for (int i = 0; i < 50; i++) {
        //            Post post = realm.createObject(Item.class, i);
        //            Post.setTitle("item no " + i);
        // }
    };

    public static void deleteRealm() {
        realm.executeTransactionAsync(realm ->
                realm.deleteAll()
        );
    }

    /**
     * Initialize Realm. Should only be done once when the application starts.
     */
    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("My_RealmDB")
                .schemaVersion(1)
                .initialData(initialData)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize Realm. Should only be done once when the application starts.
        initRealm();


        //set config api variable
        Config.apiBaseUrl = "http://192.168.1.101:8000/api/";
//        Config.apiToken = "your-api-token";
    }

}
