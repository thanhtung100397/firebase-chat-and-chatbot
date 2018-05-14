package com.ttt.chat_module.common.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttt.chat_module.R;

/**
 * Created by TranThanhTung on 24/11/2017.
 */

public class ClearableNotEditableText extends RelativeLayout implements View.OnClickListener, TextWatcher {
    public static final int DEFAULT_CLEAR_BUTTON_SIZE_DP = 20;
    public static final int DEFAULT_TEXT_PADDING_DP = 10;

    protected TextView textView;
    private Button clearButton;
    private int editTextPaddingNormal;
    private int editTextPaddingWhenClearButtonVisible;
    private boolean isButtonHideByError = false;
    private OnTextClearedListener onTextClearedListener;

    public ClearableNotEditableText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public ClearableNotEditableText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ClearableNotEditableText);

        Context context = getContext();

        textView = new TextView(context);

        int maxLength = a.getInt(R.styleable.ClearableNotEditableText_edtMaxLength, 0);
        if (maxLength > 0) {
            InputFilter[] filter = new InputFilter[1];
            filter[0] = new InputFilter.LengthFilter(maxLength);
            textView.setFilters(filter);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(R.styleable.ClearableNotEditableText_android_textSize, 18));
        textView.setTextColor(a.getColor(R.styleable.ClearableNotEditableText_android_textColor, Color.BLACK));
        int colorHint = a.getColor(R.styleable.ClearableNotEditableText_android_textColorHint, -1);
        if (colorHint != -1) {
            textView.setHintTextColor(colorHint);
        }
        textView.setHint(a.getString(R.styleable.ClearableNotEditableText_android_hint));
        textView.setGravity(a.getBoolean(R.styleable.ClearableNotEditableText_edtGravityCentered, false) ? Gravity.CENTER : Gravity.START);
        Drawable drawable = a.getDrawable(R.styleable.ClearableNotEditableText_edtBackground);
        if (drawable != null) {
            textView.setBackground(drawable);
        } else {
            int underLineColor = a.getColor(R.styleable.ClearableNotEditableText_edtUnderlineColor, -1);
            if (underLineColor != -1) {
                textView.getBackground().setColorFilter(underLineColor, PorterDuff.Mode.SRC_IN);
            }
        }
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setHorizontallyScrolling(true);
        textView.setMaxLines(1);
        textView.setFocusable(true);

        Drawable iconDrawable = a.getDrawable(R.styleable.ClearableNotEditableText_edtIcon);
        textView.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);

        editTextPaddingNormal = a.getDimensionPixelSize(R.styleable.ClearableNotEditableText_edtPadding, dpToPx(DEFAULT_TEXT_PADDING_DP, context));

        editTextPaddingNormal = a.getDimensionPixelSize(R.styleable.ClearableNotEditableText_edtPadding, dpToPx(DEFAULT_TEXT_PADDING_DP, context));

        Drawable editTextIconDrawable = a.getDrawable(R.styleable.ClearableNotEditableText_edtIcon);
        if (editTextIconDrawable != null) {
            textView.setCompoundDrawablesWithIntrinsicBounds(editTextIconDrawable, null, null, null);
            textView.setCompoundDrawablePadding(editTextPaddingNormal);
        }

        LinearLayout.LayoutParams editTextLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(editTextLp);

        clearButton = new Button(context);
        clearButton.setBackground(a.getDrawable(R.styleable.ClearableNotEditableText_clearButtonDrawable));
        int clearButtonSize = a.getDimensionPixelSize(R.styleable.ClearableNotEditableText_clearButtonSize, dpToPx(DEFAULT_CLEAR_BUTTON_SIZE_DP, context));
        LayoutParams clearButtonLp = new LayoutParams(clearButtonSize, clearButtonSize);
        clearButtonLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int clearButtonMarginRight = clearButtonSize / 2;
        clearButtonLp.setMargins(0, 0, clearButtonMarginRight, 0);
        clearButtonLp.addRule(RelativeLayout.CENTER_VERTICAL);
        clearButton.setLayoutParams(clearButtonLp);
        clearButton.setOnClickListener(this);

        editTextPaddingWhenClearButtonVisible = clearButtonSize + clearButtonMarginRight * 2;

        textView.addTextChangedListener(this);

        addView(textView);
        addView(clearButton);

        String text = a.getString(R.styleable.ClearableNotEditableText_android_text);

        textView.setText(text);

        setClickable(true);
        setFocusable(true);

        a.recycle();
    }

    public void setOnTextClearedListener(OnTextClearedListener onTextClearedListener) {
        this.onTextClearedListener = onTextClearedListener;
    }

    public void setOnEditTextClickListener(OnClickListener listener) {
        textView.setOnClickListener(listener);
    }

    public void addTextChangeListener(TextWatcher textWatcher) {
        textView.addTextChangedListener(textWatcher);
    }

    public void setError(String text) {
        if (text != null) {
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(android.R.color.white));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, text.length(), 0);

            clearButton.setVisibility(GONE);
            setClearButtonVisibility(INVISIBLE);
            isButtonHideByError = true;
            textView.setFocusableInTouchMode(true);
            textView.requestFocus();

            textView.setError(spannableStringBuilder);
        } else {
            textView.setFocusableInTouchMode(false);
            textView.setError(null);
        }
    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setText(String value) {
        textView.setText(value);
    }

    @Override
    public void onClick(View v) {
        textView.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.length() == 0 || isButtonHideByError) {
            showClearButtonWithAnimation();
            isButtonHideByError = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            hideClearButtonWithAnimation();
            if (onTextClearedListener != null) {
                onTextClearedListener.onTextCleared();
            }
        }
    }

    public void showClearButtonWithAnimation() {
        setClearButtonVisibility(VISIBLE);
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        clearButton.startAnimation(anim);
    }

    public void setClearButtonVisibility(int visibility) {
        if (visibility == VISIBLE) {
            textView.setPadding(editTextPaddingNormal, editTextPaddingNormal, editTextPaddingWhenClearButtonVisible, editTextPaddingNormal);
        } else {
            textView.setPadding(editTextPaddingNormal, editTextPaddingNormal, editTextPaddingNormal, editTextPaddingNormal);
        }
        clearButton.setVisibility(visibility);
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
                setClearButtonVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        clearButton.startAnimation(anim);
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

    public interface OnTextClearedListener {
        void onTextCleared();
    }
}