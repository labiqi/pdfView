package com.example.lcq.myapp.widgets;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lcq.myapp.R;


/**
 * 自定义加载组件的控制器
 * Created by jingtuo on 2017/6/16.
 */
public class LoadDataController {

    public static final int STYLE_BIG = 1;
    public static final int STYLE_SMALL = 2;

    private FrameLayout parent;

    private LinearLayout llProgress;
    private LinearLayout llErrorOrNoData;
    private View contentView;

    private ImageView ivIcon;
    private TextView tvMessage;
    private Button btnReload;
    private int style = STYLE_BIG;
    private OnClickReloadListener onClickReloadListener;

    private int icNoDataResId = 0;

    private int icErrorResId = 0;

    private int clickToRetryColor = -1;
    public LoadDataController(FrameLayout view) {
        parent = view;
        llProgress = (LinearLayout) view.findViewById(R.id.com_progress);
        llErrorOrNoData = (LinearLayout) view.findViewById(R.id.com_error_or_no_data);
        ivIcon = (ImageView) view.findViewById(R.id.com_iv_icon);
        tvMessage = (TextView) view.findViewById(R.id.com_tv_message);
        btnReload = (Button) view.findViewById(R.id.com_btn_reload);
        if (btnReload != null) {
            btnReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                }
            });
        }
    }

    /**
     * 显示加载中
     */
    public void showLoading() {
        llProgress.setVisibility(View.VISIBLE);
        llErrorOrNoData.setVisibility(View.GONE);
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示无数据
     *
     * @param resId
     */
    public void showNoData(int resId) {
        showNoData(parent.getContext().getString(resId));
    }

    /**
     * 显示无数据
     *
     * @param messageResId 无数据文案
     * @param remarkResId  无数据备注
     */
    public void showNoData(int messageResId, int remarkResId) {
        showNoData(parent.getContext().getString(messageResId), parent.getContext().getString(remarkResId));
    }

    /**
     * 显示无数据
     *
     * @param sequence 无数据文案
     */
    public void showNoData(CharSequence sequence) {
        showNoData(sequence, "");
    }

    /**
     * 显示无数据
     *
     * @param message 无数据文案
     * @param remark  无数据备注
     */
    public void showNoData(CharSequence message, CharSequence remark) {
        llProgress.setVisibility(View.GONE);
        llErrorOrNoData.setVisibility(View.VISIBLE);
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
        if (icNoDataResId != 0) {
            ivIcon.setImageResource(icNoDataResId);
        }
        if (STYLE_BIG == style) {
            if (btnReload != null) {
                btnReload.setVisibility(View.GONE);
            }
        }
        tvMessage.setText(message);
    }


    /**
     * 显示错误信息
     *
     * @param resId
     */
    public void showError(int resId) {
        showError(parent.getContext().getString(resId));
    }

    /**
     * 显示错误信息
     *
     * @param sequence
     */
    public void showError(CharSequence sequence) {
        llProgress.setVisibility(View.GONE);
        llErrorOrNoData.setVisibility(View.VISIBLE);
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
        if (icErrorResId != 0) {
            ivIcon.setImageResource(icErrorResId);
        }

        if (STYLE_BIG == style) {
            tvMessage.setText(sequence);
            if (btnReload != null) {
                btnReload.setVisibility(View.VISIBLE);
            }
        } else if (STYLE_SMALL == style) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            if (!TextUtils.isEmpty(sequence)) {
                builder.append(sequence);
                builder.append("，");
            }
            int start = builder.length();
            builder.append(parent.getResources().getString(R.string.click_to_retry));
            int end = builder.length();
            builder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    reload();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    if (clickToRetryColor != -1) {
                        ds.setColor(clickToRetryColor);
                    }
                    ds.setUnderlineText(false);
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
            tvMessage.setText(builder);
        } else {
            ivIcon.setImageResource(0);
            tvMessage.setText(sequence);
        }
    }

    /**
     * 设置要显示的内容
     *
     * @param resId
     */
    public void setContentView(int resId) {
        setContentView(View.inflate(parent.getContext(), resId, null));
    }

    /**
     * 设置要显示的内容
     *
     * @param contentView
     */
    public void setContentView(View contentView) {
        this.contentView = contentView;
        parent.addView(contentView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 显示内容
     */
    public void showContentView() {
        llProgress.setVisibility(View.GONE);
        llErrorOrNoData.setVisibility(View.GONE);
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnClickReloadListener {
        void onClickReload();
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(int id) {
        return parent.findViewById(id);
    }

    /**
     * 不显示任何内容
     */
    public void showEmptyView() {
        llProgress.setVisibility(View.GONE);
        llErrorOrNoData.setVisibility(View.GONE);
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public void setOnClickReloadListener(OnClickReloadListener onClickReloadListener) {
        this.onClickReloadListener = onClickReloadListener;
    }

    private void reload() {
        showLoading();
        parent.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onClickReloadListener != null) {
                    onClickReloadListener.onClickReload();
                }
            }
        }, 500L);
    }

    public void setIcNoDataResId(int icNoDataResId) {
        this.icNoDataResId = icNoDataResId;
    }

    public void setIcErrorResId(int icErrorResId) {
        this.icErrorResId = icErrorResId;
    }

    public void setClickToRetryColor(int clickToRetryColor) {
        this.clickToRetryColor = clickToRetryColor;
    }

    public void setMsgTextColor(int color) {
        tvMessage.setTextColor(color);
    }
}
