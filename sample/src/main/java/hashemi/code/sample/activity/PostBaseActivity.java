package hashemi.code.sample.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import hashemi.code.sample.model.Post;
import hashemi.code.sample.model.PostInterface;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public abstract class PostBaseActivity extends AppCompatActivity implements PostInterface {
    protected BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceive(context, intent);
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void onBroadcastReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.net.conn.CONNECTIVITY_CHANGE")) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            onConnectionStatusChanged(networkInfo != null && networkInfo.isConnected());
        }
    }

    protected abstract void onConnectionStatusChanged(boolean isConnected);

    @Override
    public void onDataResponse(Post post) {

    }

    @Override
    public void onDataResponse(String jsonResponse) {

    }

    @Override
    public void onDataListResponse(List<Post> lstT, int pageNo, int pageCount) {

    }

    @Override
    public void onDataListResponse(String jsonResponse) {

    }

    @Override
    public void onDeleteResponse(boolean done) {

    }

    @Override
    public void onConnectionError(Exception e) {

    }

    @Override
    public void onStartRequest() {

    }

    @Override
    public void onTimeOut(Exception e) {

    }

    @Override
    public void onForbiddenAccess(int code) {

    }

    @Override
    public void onUnauthorized(Exception e) {

    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onFinishRequest() {

    }
}