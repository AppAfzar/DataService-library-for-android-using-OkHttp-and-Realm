package hashemi.code.sample.model;

import android.content.Context;

import appafzar.dataservice.realm.BaseRealmModel;
import appafzar.dataservice.web.model.BaseModel;
import io.realm.Realm;

/**
 * There is all usual methods to request data are added in DataModel
 * bur you can add more methods to request data from server in this model for 'Item' entity
 * <p>
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class PostModel extends BaseRealmModel<Post> {

    /**
     * @param context app or activity context.
     * @see BaseModel :
     */
    public PostModel(Context context, Realm realm, PostInterface postInterface) {
        super(context, realm, Post.class);
        web.setControllerName("post");
    }

}
