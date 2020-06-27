package hashemi.code.sample.presenter;

import android.content.Context;

import appafzar.dataservice.web.model.BaseModel;
import appafzar.dataservice.web.model.DataInterface;
import appafzar.dataservice.web.presenter.BasePresenter;
import hashemi.code.sample.App;
import hashemi.code.sample.model.Post;
import hashemi.code.sample.model.PostInterface;
import hashemi.code.sample.model.PostModel;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostPresenter extends BasePresenter<Post> implements PostInterface {
    private PostModel model;
    private Context context;

    public PostPresenter(Context context, DataInterface<Post> view) {
        super(context, view);
        this.context = context;
    }

    @Override
    protected BaseModel<Post> setDataModel(Context context) {
        model = new PostModel(context, App.realm, this);
        return model;
    }

    @Override
    public void onDataResponse(String jsonResponse) {
        model.createOrUpdateLocal(jsonResponse);
    }

    @Override
    public void onDataListResponse(String jsonResponse) {
        model.createOrUpdateAllLocal(jsonResponse);
    }
}
