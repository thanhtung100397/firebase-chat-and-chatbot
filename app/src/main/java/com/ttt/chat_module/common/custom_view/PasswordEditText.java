package com.ttt.chat_module.common.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ttt.chat_module.R;

/**
 * Created by TranThanhTung on 24/11/2017.
 */

public class PasswordEditText extends RelativeLayout implements View.OnClickListener, TextWatcher {
    public static final int DEFAULT_CLEAR_BUTTON_SIZE_DP = 24;
    public static final int DEFAULT_TEXT_PADDING_DP = 10;

    private static final int MODE_SHOW_PASSWORD = 0;
    private static final int MODE_HIDE_PASSWORD = 1;

    private EditText editText;
    private Button showHideButton;
    private int editTextPaddingNormal;
    private int editTextPaddingWhenShowHideButtonVisible;
    private boolean isButtonHideByError = false;

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordEditText);

        Context context = getContext();

        editText = new EditText(context);

        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(R.styleable.PasswordEditText_android_textSize, 18));
        editText.setTextColor(a.getColor(R.styleable.PasswordEditText_android_textColor, Color.BLACK));
        editText.setHint(a.getString(R.styleable.PasswordEditText_android_hint));
        editText.setGravity(a.getBoolean(R.styleable.PasswordEditText_edtGravityCentered, false) ? Gravity.CENTER : Gravity.START);
        Drawable drawable = a.getDrawable(R.styleable.PasswordEditText_edtBackground);
        if (drawable != null) {
            editText.setBackground(drawable);
        } else {
            int underLineColor = a.getColor(R.styleable.PasswordEditText_edtUnderlineColor, -1);
            if (underLineColor != -1) {
                editText.getBackground().setColorFilter(underLineColor, PorterDuff.Mode.SRC_IN);
            }
        }

        editText.setHorizontallyScrolling(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        editTextPaddingNormal = a.getDimensionPixelSize(R.styleable.PasswordEditText_edtPadding, dpToPx(DEFAULT_TEXT_PADDING_DP, context));

        Drawable editTextIconDrawable = a.getDrawable(R.styleable.PasswordEditText_edtIcon);
        if (editTextIconDrawable != null) {
            editText.setCompoundDrawablesWithIntrinsicBounds(editTextIconDrawable, null, null, null);
            editText.setCompoundDrawablePadding(editTextPaddingNormal);
        }

        LinearLayout.LayoutParams editTextLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextLp);

        showHideButton = new Button(context);

        showHideButton.setBackground(a.getDrawable(R.styleable.PasswordEditText_toggleButtonDrawable));
        int clearButtonSize = a.getDimensionPixelSize(R.styleable.PasswordEditText_toggleButtonSize, dpToPx(DEFAULT_CLEAR_BUTTON_SIZE_DP, context));

        LayoutParams showHideButtonLp = new LayoutParams(clearButtonSize, clearButtonSize);
        showHideButtonLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int clearButtonMarginRight = clearButtonSize / 2;
        showHideButtonLp.setMargins(0, 0, 0, 0);
        showHideButtonLp.addRule(RelativeLayout.CENTER_VERTICAL);

        showHideButton.setLayoutParams(showHideButtonLp);
        showHideButton.setOnClickListener(this);

        editTextPaddingWhenShowHideButtonVisible = clearButtonSize + clearButtonMarginRight * 2;

        editText.addTextChangedListener(this);

        addView(editText);
        addView(showHideButton);

        String text = a.getString(R.styleable.ClearableEditText_android_text);

        editText.setText(text);

        a.recycle();
    }

    public void setError(String text) {
        setButtonVisibility(INVISIBLE);
        isButtonHideByError = false;
        editText.setError(text);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String value) {
        editText.setText(value);
    }

    @Override
    public void onClick(View v) {
        Drawable backgroundDrawable = v.getBackground();
        int cursorPosition = editText.getSelectionStart();
        switch (backgroundDrawable.getLevel()) {
            case MODE_HIDE_PASSWORD:{
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                backgroundDrawable.setLevel(MODE_SHOW_PASSWORD);
            }
            break;

            default:{
                editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                backgroundDrawable.setLevel(MODE_HIDE_PASSWORD);
            }
        }
        editText.setSelection(cursorPosition);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.length() == 0 || isButtonHideByError) {
            isButtonHideByError = false;
            showButtonWithAnimation();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            hideClearButtonWithAnimation();
        }
    }

    public void showButtonWithAnimation() {
        setButtonVisibility(VISIBLE);
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        showHideButton.startAnimation(anim);
    }

    public void setButtonVisibility(int visibility) {
        if (visibility == VISIBLE) {
            editText.setPadding(0, editTextPaddingNormal, editTextPaddingWhenShowHideButtonVisible, editTextPaddingNormal);
        } else {
            editText.setPadding(0, editTextPaddingNormal, 0, editTextPaddingNormal);
        }
        showHideButton.setVisibility(visibility);
    }

    public void hideClearButtonWithAnimation() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setButtonVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showHideButton.startAnimation(anim);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}