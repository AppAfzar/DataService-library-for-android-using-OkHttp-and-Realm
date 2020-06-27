package appafzar.dataservice.web.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import appafzar.dataservice.helper.Log;
import okhttp3.Call;
import okhttp3.Response;

abstract class BaseResponseHandler implements ResponseInterface {

    private static final int START_MESSAGE = 0;
    private static final int FINISH_MESSAGE = 1;
    private static final int TIMEOUT_MESSAGE = 2;
    private static final int SUCCESS_MESSAGE = 3;
    private static final int FAILURE_MESSAGE = 4;
    private static final int UNAUTHORIZED_MESSAGE = 5;
    private static final int FORBIDDEN_ACCESS = 6;
    public static String ERROR_NULL_OR_EMPTY = "null_or_empty";
    //region Property
    /**
     * A variable for keeping converted raw response
     * in order to use id UI thread later.
     */
    private byte[] byteResponse = null;
    private boolean useSynchronousMode;
    private boolean usePoolThread;
    private Handler handler;
    private Looper looper = null;

    /**
     * Creates a new BaseResponseHandler
     */
    public BaseResponseHandler() {
        this(null);
    }

    /**
     * Creates a new BaseResponseHandler with a user-supplied looper. If
     * the passed looper is null, the looper attached to the current thread will
     * be used.
     *
     * @param looper The looper to work with
     */
    public BaseResponseHandler(Looper looper) {
        this.looper = looper == null ? Looper.myLooper() : looper;

        // Use asynchronous mode by default.
        setUseSynchronousMode(false);

        // Do not use the pool's thread to fire callbacks by default.
        setUsePoolThread(false);
    }

    public BaseResponseHandler(boolean usePoolThread) {
        // Whether to use the pool's thread to fire callbacks.
        setUsePoolThread(usePoolThread);

        // When using the pool's thread, there's no sense in having a looper.
        if (!getUsePoolThread()) {
            // Use the current thread's looper.
            this.looper = Looper.myLooper();

            // Use asynchronous mode by default.
            setUseSynchronousMode(false);
        }
    }

    public boolean getUseSynchronousMode() {
        return useSynchronousMode;
    }

    public void setUseSynchronousMode(boolean sync) {
        // A looper must be prepared before setting asynchronous mode.
        if (!sync && looper == null) {
            sync = true;
            //.log.w(LOG_TAG, "Current thread has not called Looper.prepare(). Forcing synchronous mode.");
        }

        // If using asynchronous mode.
        if (!sync && handler == null) {
            // Create a handler on current thread to submit tasks
            handler = new BaseResponseHandler.ResponderHandler(this, looper);
        } else if (sync && handler != null) {
            // TODO: Consider adding a flag to remove all queued messages.
            handler = null;
        }

        useSynchronousMode = sync;
    }

    public boolean getUsePoolThread() {
        return usePoolThread;
    }

    public void setUsePoolThread(boolean pool) {
        // If pool thread is to be used, there's no point in keeping a reference
        // to the looper and no need for a handler.
        if (pool) {
            looper = null;
            handler = null;
        }

        usePoolThread = pool;
    }
    //endregion

    //region UserMessage
    // Methods which emulate android's Handler and UserMessage methods
    private void handleMessage(Message message) {
        Object[] response;

        try {
            switch (message.what) {
                case SUCCESS_MESSAGE:
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 2) {
                        invokeOnSuccess((Call) response[0], (Response) response[1]);
                    } else {
                        invokeOnFailure(new Exception("SUCCESS_MESSAGE didn't got enough params"));
                    }
                    break;

                case FAILURE_MESSAGE:
                    invokeOnFailure((Exception) message.obj);
                    break;

                case START_MESSAGE:
                    invokeOnStart();
                    break;

                case FINISH_MESSAGE:
                    invokeOnFinish();
                    break;

                case UNAUTHORIZED_MESSAGE:
                    invokeOnUnauthorized((Exception) message.obj);
                    break;

                case TIMEOUT_MESSAGE:
                    invokeOnTimeOut((Exception) message.obj);
                    break;

                case FORBIDDEN_ACCESS:
                    invokeOnForbiddenAccess((int) message.obj);
                    break;
            }
        } catch (Throwable error) {
            Log.e(error);
        }
    }


    /**
     * Helper method to send runnable into local handler loop
     *
     * @param runnable runnable instance, can be null
     */
    protected void postRunnable(Runnable runnable) {
        if (runnable != null) {
            if (getUseSynchronousMode() || handler == null) {
                // This response handler is synchronous, run on current thread
                runnable.run();
            } else {
                // Otherwise, run on provided handler
                handler.post(runnable);
            }
        }
    }

    public final void sendSuccessMessage(Call call, Response response) {
        convertResponse(response);
        sendMessage(
                obtainMessage(SUCCESS_MESSAGE, new Object[]{call, response})
        );
    }

    public final void sendFailureMessage(Exception e) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, e));
    }

    public final void sendTimeOutMessage(Exception e) {
        sendMessage(obtainMessage(TIMEOUT_MESSAGE, e));
    }

    public void sendUnauthorizedMessage(Exception e) {
        Log.e(e);
        sendMessage(obtainMessage(UNAUTHORIZED_MESSAGE, e));
    }

    public void sendForbiddenAccessMessage(int code) {
        sendMessage(obtainMessage(FORBIDDEN_ACCESS, code));
    }

    final public void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    final public void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    /**
     * Helper method to create UserMessage instance from handler
     *
     * @param responseMessageId   constant to identify Handler message
     * @param responseMessageData object to be passed to message receiver
     * @return UserMessage instance, should not be null
     */
    private Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handler, responseMessageId, responseMessageData);
    }

    protected void sendMessage(Message msg) {
        if (getUseSynchronousMode() || handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) { // do not send messages if request has been cancelled
            //Utils.asserts(handler != null, "handler should not be null!");
            handler.sendMessage(msg);
        }
    }
    //endregion

    //region response
    public void onSuccess(Call call, Response response) {

    }
    //endregion

    //region Invoke
    private void invokeOnFinish() {
        try {
            onFinishRequest();
        } catch (Exception ex) {
            Log.e(ex);
        }
    }

    private void invokeOnStart() {
        try {
            onStartRequest();
        } catch (Exception ex) {
            Log.e(ex);
        }
    }

    void invokeOnFailure(Exception e) {
        try {
            onFailure(e);
        } catch (Exception ex) {
            Log.e(ex);
        }
    }

    private void invokeOnUnauthorized(Exception e) {
        try {
            onUnauthorized(e);
        } catch (Exception ex) {
            Log.e(ex);
        }
    }

    private void invokeOnTimeOut(Exception e) {
        try {
            onTimeOut(e);
        } catch (Exception ex) {
            Log.e(ex);
        }
    }


    private void invokeOnForbiddenAccess(int code) {
        try {
            onForbiddenAccess(code);
        } catch (Exception ex) {
            Log.e(ex);
        }
    }

    protected void invokeOnSuccess(Call call, Response response) {
        try {
            onSuccess(call, response);
        } catch (Exception ex) {
            invokeOnFailure(ex);
        }
    }
    //endregion

    //region Convert response

    /**
     * Get the response which is converted in a background thread before by calling {@link #convertResponse(Response)} .
     *
     * @return byte[] converted raw response
     */
    public final byte[] getByteResponse() {
        return byteResponse;
    }

    /**
     * Converting response in a background thread and keep its result in to a variable in order to
     * use it in UI thread later.
     *
     * @param response Raw response
     */
    public void convertResponse(Response response) {
        try {
            byteResponse = response.body().bytes();
            response.close();
        } catch (Exception ex) {
            sendFailureMessage(ex);
        }
    }

    /**
     * Avoid leaks by using a non-anonymous handler class.
     */
    private static class ResponderHandler extends Handler {
        private final BaseResponseHandler mResponder;

        ResponderHandler(BaseResponseHandler mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }
    //endregion
}
