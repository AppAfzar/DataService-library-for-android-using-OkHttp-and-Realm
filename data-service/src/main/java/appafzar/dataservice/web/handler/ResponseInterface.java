package appafzar.dataservice.web.handler;

public interface ResponseInterface {

    void onStartRequest();

    void onTimeOut(Exception e);

    void onForbiddenAccess(int code);

    void onUnauthorized(Exception e);

    void onFailure(Exception e);

    void onFinishRequest();

//    void onSuccess(Call call, Response response);

}
