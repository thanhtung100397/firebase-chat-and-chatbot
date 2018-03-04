package com.ttt.chat_module.views.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ttt.chat_module.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by TranThanhTung on 02/02/2018.
 */
public class LoadingFragment extends Fragment {
    private LinearLayout lnRetry;
    private AVLoadingIndicatorView loadingIndicatorView;
    private OnRetryListener onRetryListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        loadingIndicatorView = rootView.findViewById(R.id.loading_indicator);
        lnRetry = rootView.findViewById(R.id.ln_retry);
        rootView.findViewById(R.id.btn_retry).setOnClickListener(view -> {
            if (onRetryListener != null){
                onRetryListener.onRetry(this);
            }
        });
        return rootView;
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public void showProgress() {
        loadingIndicatorView.setVisibility(View.VISIBLE);
        lnRetry.setVisibility(View.GONE);
    }

    public void showError() {
        loadingIndicatorView.setVisibility(View.GONE);
        lnRetry.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        loadingIndicatorView.setVisibility(View.GONE);
    }

    public interface OnRetryListener {
        void onRetry(LoadingFragment loadingFragment);
    }
}
