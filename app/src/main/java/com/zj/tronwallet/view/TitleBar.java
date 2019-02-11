package com.zj.tronwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.tronwallet.R;
import com.zj.tronwallet.utils.DisplayUtils;

import java.lang.reflect.Method;


/**
 * 界面标题栏
 *
 * @author Created by jiang on 2017/7/31.
 */

public class TitleBar extends RelativeLayout {

    private TextView mTitleTv;
    private TextView mLeftBtn;
    private TextView mRightBtn1;
    private TextView mRightBtn2;
    private View mDividerLine;

    /**
     * 反射获取执行方法
     */
    private Object mPresenter;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_titlebar, this, true);
        mTitleTv = findViewById(R.id.titlebar_title_tv);
        mLeftBtn = findViewById(R.id.titlebar_left_btn);
        mRightBtn1 = findViewById(R.id.titlebar_right_btn_1);
        mRightBtn2 = findViewById(R.id.titlebar_right_btn_2);
        mDividerLine = findViewById(R.id.titlebar_divider_line);
        initViewAttrs(context, attrs, defStyleAttr);
        setPresenter(context);
    }

    private void initViewAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        int defaultColor = 0xff333333;
        int sp16 = DisplayUtils.dp2px(context, 16);
        int sp14 = DisplayUtils.dp2px(context, 14);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);

        String titleText = a.getString(R.styleable.TitleBar_titleText);
        if (!TextUtils.isEmpty(titleText)) {
            setTitleText(titleText);
        }
        int titleColor = a.getColor(R.styleable.TitleBar_titleColor, defaultColor);
        setTitleColor(titleColor);
        int titleSize = a.getDimensionPixelSize(R.styleable.TitleBar_titleSize, sp16);
        setTitleSize(titleSize);

        String leftBtnText = a.getString(R.styleable.TitleBar_leftBtnText);
        if (!TextUtils.isEmpty(leftBtnText)) {
            setLeftBtnText(leftBtnText);
        }
        int leftBtnTextColor = a.getColor(R.styleable.TitleBar_leftBtnTextColor, defaultColor);
        setLeftBtnTextColor(leftBtnTextColor);
        int leftBtnTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_leftBtnTextSize, sp14);
        setLeftBtnTextSize(leftBtnTextSize);
        Drawable leftBtnIcon = a.getDrawable(R.styleable.TitleBar_leftBtnIcon);
        if (leftBtnIcon != null) {
            setLeftBtnIcon(leftBtnIcon);
        }
        String leftBtnClick = a.getString(R.styleable.TitleBar_leftBtnClick);
        if (!TextUtils.isEmpty(leftBtnClick)) {
            setLeftBtnClick(new DeclaredOnClickListener(leftBtnClick));
        }

        String rightBtn1Text = a.getString(R.styleable.TitleBar_rightBtn1Text);
        if (!TextUtils.isEmpty(rightBtn1Text)) {
            setRightBtn1Text(rightBtn1Text);
        }
        int rightBtn1TextColor = a.getColor(R.styleable.TitleBar_rightBtn1TextColor, defaultColor);
        setRightBtn1TextColor(rightBtn1TextColor);
        int rightBtn1TextSize = a.getDimensionPixelSize(R.styleable.TitleBar_rightBtn1TextSize, sp14);
        setRightBtn1TextSize(rightBtn1TextSize);
        Drawable rightBtn1Icon = a.getDrawable(R.styleable.TitleBar_rightBtn1Icon);
        if (rightBtn1Icon != null) {
            setRightBtn1Icon(rightBtn1Icon);
        }
        String rightBtn1Click = a.getString(R.styleable.TitleBar_rightBtn1Click);
        if (rightBtn1Click != null) {
            setRightBtn1Click(new DeclaredOnClickListener(rightBtn1Click));
        }

        String rightBtn2Text = a.getString(R.styleable.TitleBar_rightBtn2Text);
        if (!TextUtils.isEmpty(rightBtn2Text)) {
            setRightBtn2Text(rightBtn2Text);
        }
        int rightBtn2TextColor = a.getColor(R.styleable.TitleBar_rightBtn2TextColor, defaultColor);
        setRightBtn2TextColor(rightBtn2TextColor);
        int rightBtn2TextSize = a.getDimensionPixelSize(R.styleable.TitleBar_rightBtn2TextSize, sp14);
        setRightBtn2TextSize(rightBtn2TextSize);
        Drawable rightBtn2Icon = a.getDrawable(R.styleable.TitleBar_rightBtn2Icon);
        if (rightBtn2Icon != null) {
            setRightBtn2Icon(rightBtn2Icon);
        }
        String rightBtn2Click = a.getString(R.styleable.TitleBar_rightBtn2Click);
        if (rightBtn2Click != null) {
            setRightBtn2Click(new DeclaredOnClickListener(rightBtn2Click));
        }

        boolean showDivider = a.getBoolean(R.styleable.TitleBar_showDividerLine, false);
        showDividerLine(showDivider);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public TitleBar setTitleText(String text) {
        mTitleTv.setText(text);
        return this;
    }

    public TitleBar setTitleColor(int color) {
        mTitleTv.setTextColor(color);
        return this;
    }

    public TitleBar setTitleSize(int size) {
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setLeftBtnText(String text) {
        mLeftBtn.setText(text);
        if (TextUtils.isEmpty(text) && mLeftBtn.getCompoundDrawables()[0] == null) {
            mLeftBtn.setVisibility(INVISIBLE);
        } else {
            mLeftBtn.setVisibility(VISIBLE);
        }
        return this;
    }

    public TitleBar setLeftBtnTextColor(int color) {
        mLeftBtn.setTextColor(color);
        return this;
    }

    public TitleBar setLeftBtnTextSize(int size) {
        mLeftBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setLeftBtnIcon(Drawable icon) {
        mLeftBtn.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        if (icon == null && TextUtils.isEmpty(mLeftBtn.getText())) {
            mLeftBtn.setVisibility(INVISIBLE);
        } else {
            mLeftBtn.setVisibility(VISIBLE);
        }
        return this;
    }

    public TitleBar setLeftBtnClick(OnClickListener click) {
        mLeftBtn.setOnClickListener(click);
        return this;
    }

    public TitleBar setRightBtn1Text(String text) {
        mRightBtn1.setText(text);
        if (TextUtils.isEmpty(text) && mRightBtn1.getCompoundDrawables()[0] == null) {
            mRightBtn1.setVisibility(INVISIBLE);
        } else {
            mRightBtn1.setVisibility(VISIBLE);
        }
        return this;
    }

    public TitleBar setRightBtn1TextColor(int color) {
        mRightBtn1.setTextColor(color);
        return this;
    }

    public TitleBar setRightBtn1TextSize(int size) {
        mRightBtn1.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setRightBtn1Icon(Drawable icon) {
        mRightBtn1.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        if (icon == null && TextUtils.isEmpty(mRightBtn1.getText())) {
            mRightBtn1.setVisibility(INVISIBLE);
        } else {
            mRightBtn1.setVisibility(VISIBLE);
        }
        return this;
    }

    public TitleBar setRightBtn1Click(OnClickListener click) {
        mRightBtn1.setOnClickListener(click);
        return this;
    }

    public TitleBar setRightBtn2Text(String text) {
        mRightBtn2.setText(text);
        if (TextUtils.isEmpty(text) && mRightBtn2.getCompoundDrawables()[0] == null) {
            mRightBtn2.setVisibility(INVISIBLE);
        } else {
            mRightBtn2.setVisibility(VISIBLE);
        }
        return this;
    }

    public TitleBar setRightBtn2TextColor(int color) {
        mRightBtn2.setTextColor(color);
        return this;
    }

    public TitleBar setRightBtn2TextSize(int size) {
        mRightBtn2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setRightBtn2Icon(Drawable icon) {
        mRightBtn2.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        if (icon == null && TextUtils.isEmpty(mRightBtn2.getText())) {
            mRightBtn2.setVisibility(INVISIBLE);
        } else {
            mRightBtn2.setVisibility(VISIBLE);
        }
        return this;
    }

    public TitleBar setRightBtn2Click(OnClickListener click) {
        mRightBtn2.setOnClickListener(click);
        return this;
    }

    public TitleBar showDividerLine(boolean show) {
        mDividerLine.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    public void setPresenter(Object presenter) {
        mPresenter = presenter;
    }

    private class DeclaredOnClickListener implements OnClickListener {
        private final String mMethodName;
        private Method mResolvedMethod;

        private DeclaredOnClickListener(String methodName) {
            mMethodName = methodName;
        }

        @Override
        public void onClick(View v) {
            if (mResolvedMethod == null) {
                try {
                    mResolvedMethod = mPresenter.getClass().getMethod(mMethodName, View.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException("查找点击事件方法失败: " + mMethodName);
                }
            }

            try {
                mResolvedMethod.invoke(mPresenter, v);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("执行点击事件方法失败: " + mMethodName);
            }
        }
    }
}
