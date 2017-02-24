package progress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import com.example.math.newsever.R;

/**
 * Created by liukun on 16/3/10.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;
    private FrameLayout pdf;
    private Activity context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    private static int progress_index;
    private static Object lock = new Object();

    public ProgressDialogHandler(Activity context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        if (progress_index == 0) {
            if (pdf == null) {
                pdf = (FrameLayout) context.findViewById(R.id.framCartProgress);
            }
            pdf.setVisibility(View.VISIBLE);
        }
        synchronized (lock) {
            progress_index++;
        }
    }

    private void dismissProgressDialog() {
        mProgressCancelListener.onCancelProgress();
        synchronized (lock) {
            progress_index--;
        }
        if (progress_index == 0) {
            if (pdf != null) {
                pdf.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
