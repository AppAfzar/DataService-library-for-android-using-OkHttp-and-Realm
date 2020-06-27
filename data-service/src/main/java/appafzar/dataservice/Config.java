package appafzar.dataservice;

public class Config {

    // A Note on HTTP Status Codes and the Response Format
    public static final int SERVER_BAD_REQUEST = 400;

    //region API CONFIG
    public static final String RESPONSE_PAGE_SIZE = "per_page";
    public static final String RESPONSE_PAGE_NUMBER = "current_page";
    public static final String RESPONSE_PAGE_TOTAL_COUNT = "last_page";
    public static final String RESPONSE_KEY_DATA = "data";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_READ_ALL = "read_all";
    public static final String ACTION_READ = "read";
    public static final String ACTION_DELETE = "delete";
    public static final int SERVER_UNAUTHORIZED_USER = 401;
    public static final int SERVER_FORBIDDEN_ACCESS = 403;
    public static final int SERVER_URL_NOT_FOUND = 404;
    public static final int SERVER_INTERNAL_ERROR = 500;
    public static final int SERVER_SERVICE_UNAVAILABLE = 503;
    //Complete API base url (with a "/" at end)
    public static String apiBaseUrl = "";
    //Important: Set apiToken in start of application with stored apiToken.
    public static String apiToken = null;
    //Web Connection timeout in seconds:
    public static int connection_timeout = 30;
    public static int connection_read_timeout = 60;
    public static int connection_write_timeout = 90;
    public static int call_timeout = 30;

    public static void setConnectionTimeout(int connect, int read, int write, int call) {
        connection_timeout = connect;
        connection_read_timeout = read;
        connection_write_timeout = write;
        call_timeout = call;
    }

    //endregion

}
