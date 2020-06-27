package hashemi.code.sample.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Post extends RealmObject {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final int PEGINATION_PER_PAGE = 15;

    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;


    /**
     * This method should be run in a Realm transaction
     *
     * @param realm
     */
    public static void create(Realm realm) {
        long i = realm.where(Post.class).count() + 1;
        Post item = realm.createObject(Post.class, i);
        item.setTitle("Item no " + i);

    }

    /**
     * This method should be run in a Realm transaction
     *
     * @param realm
     */
    public static void delete(Realm realm, long id) {
        Post post = realm.where(Post.class).equalTo(FIELD_ID, id).findFirst();
        // Otherwise it has been deleted already.
        if (post != null) {
            post.deleteFromRealm();
        }
    }

    //region Setter & Getter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    //endregion
}
