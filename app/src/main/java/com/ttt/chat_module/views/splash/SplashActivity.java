package com.ttt.chat_module.views.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.common.utils.Utils;
import com.ttt.chat_module.presenters.splash.SplashPresenter;
import com.ttt.chat_module.presenters.splash.SplashPresenterImpl;
import com.ttt.chat_module.views.auth.login.LoginActivity;
import com.ttt.chat_module.views.base.activity.BaseActivity;
import com.ttt.chat_module.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {
    @BindView(R.id.loading_bar)
    RoundCornerProgressBar loadingBar;

    private boolean isRequestSuccess = false;
    private AsyncTask<Void, Integer, Void> splashTimer;

    @Override
    protected SplashPresenter initPresenter() {
        return new SplashPresenterImpl(this);
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (UserAuth.isUserLoggedIn()) {
            if(Utils.isInternetAvailable(this)) {
                getPresenter().goToOnlineState();
            } else {
                completeLoading();
            }
            splashTimer = new SplashTimer().execute();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void completeLoading() {
        isRequestSuccess = true;
    }

    @Override
    public void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_occurred)
                .setMessage(R.string.unexpected_error_occurred)
                .setCancelable(false)
                .setPositiveButton(R.string.retry, (dialogInterface, i) -> {
                    getPresenter().goToOnlineState();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(splashTimer != null && !splashTimer.isCancelled()) {
            splashTimer.cancel(true);
            splashTimer = null;
        }
    }

    private class SplashTimer extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            loadingBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long millisPerProgress = Constants.TIME_SPLASH_SCREEN / 100;
            int progress = 0;
            try {
                while (progress <= 80) {
                    progress++;
                    publishProgress(progress);
                    Thread.sleep(millisPerProgress);
                }
                while (progress <= 100) {
                    progress++;
                    publishProgress(progress);
                    Thread.sleep(millisPerProgress);
                }
            } catch (InterruptedException ignored) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
