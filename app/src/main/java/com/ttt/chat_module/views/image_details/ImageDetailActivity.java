package com.ttt.chat_module.views.image_details;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;
import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.GlideRequest;
import com.ttt.chat_module.GlideRequests;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.views.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.img_zoomable)
    ZoomageView zoomageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.ln_retry)
    LinearLayout lnError;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle(R.string.view_image_detail);
        }

        findViewById(R.id.btn_retry).setOnClickListener(view -> {
            lnError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        });

        GlideRequests glideRequests = GlideApp.with(this);
        String imageUriString = getIntent().getStringExtra(Constants.KEY_IMAGE_URI);
        GlideRequest<Drawable> glideRequest;
        if (imageUriString == null) {
            glideRequest = glideRequests.load(getIntent().getStringExtra(Constants.KEY_IMAGE_URL));
        } else {
            glideRequest = glideRequests.load(Uri.parse(imageUriString));
        }
        glideRequest.placeholder(R.drawable.image_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        lnError.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(zoomageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;

            case R.id.action_download:{

            }
            break;

            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
