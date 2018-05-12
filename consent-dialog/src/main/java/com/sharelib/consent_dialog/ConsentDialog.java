package com.sharelib.consent_dialog;

import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsentDialog extends DialogFragment {

    /**
     * Tag Constant
     */
    public static final String TAG = ConsentDialog.class.getSimpleName();

    /**
     * Argument Attributes
     */
    private static final String PERMISSION_NAME_COLOR = "permission_name_color";
    private static final String PERMISSION_NAME_SIZE = "permission_name_size";
    private static final String TITLE_STRING = "title_string";
    private static final String NEXT_STRING = "next_string";
    private static final String NEXT_TEXT_COLOR = "next_text_color";
    private static final String CARD_BACKGROUND_COLOR = "card_background_color";
    private static final String TITLE_COLOR = "title_color";
    private static final String TITLE_SIZE = "title_size";
    private static final String NEXT_SIZE = "next_size";
    private static final String NEXT_BACKGROUND_COLOR = "next_background_color";

    /**
     * Instance Methods
     */
    public static ConsentDialog newInstance(List<PermissionItem> list,
                                            @ColorInt int permissionNameColor, float permissionNameSize,
                                            String titleString, @ColorInt int titleColor, String nextString,
                                            @ColorInt int nextTextColor, @ColorInt int cardBackgroundColor, float titleSize,
                                            int nextSize, @ColorInt int nextBackgroundColor, ConsentCallback callback) {
        ConsentDialog dialog = new ConsentDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(PERMISSION_NAME_COLOR, permissionNameColor);
        bundle.putFloat(PERMISSION_NAME_SIZE, permissionNameSize);
        bundle.putInt(TITLE_COLOR, titleColor);
        bundle.putFloat(TITLE_SIZE, titleSize);
        bundle.putString(TITLE_STRING, titleString);
        bundle.putString(NEXT_STRING, nextString);
        bundle.putInt(NEXT_TEXT_COLOR, nextTextColor);
        bundle.putInt(NEXT_BACKGROUND_COLOR, nextBackgroundColor);
        bundle.putInt(NEXT_SIZE, nextSize);
        bundle.putInt(CARD_BACKGROUND_COLOR, cardBackgroundColor);
        dialog.setArguments(bundle);
        dialog.setPermissions(list);
        dialog.setCallback(callback);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * Data
     */
    private List<PermissionItem> mList;
    private ConsentCallback mCallback;

    /**
     * UI Widgets
     */
    private View mView;

    /**
     * DialogFragment Methods
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setRetainInstance(true);

        mView = inflater.inflate(R.layout.consent_dialog, container, false);

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        if (savedInstanceState == null) {
            List<PermissionItem> tempList = new ArrayList<>();
            for (PermissionItem item : mList) {
                for (String permission : item.getPermissions()) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        tempList.add(item);
                        break;
                    }
                }
            }
            mList = tempList;
        }

        initUIWidgets();

        return mView;
    }

    /**
     * Class Methods
     */
    private void initUIWidgets() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            throw new IllegalArgumentException("NO ARGUMENTS WERE SET TO CONSENT DIALOG!");
        }

        int defaultTextColor = ContextCompat.getColor(getContext(), R.color.cardview_dark_background);
        int defaultWhiteColor = ContextCompat.getColor(getContext(), android.R.color.white);

        TextView title = mView.findViewById(R.id.cod_title);
        title.setText(bundle.getString(TITLE_STRING));
        title.setTextSize(bundle.getFloat(TITLE_SIZE, 14));
        title.setTextColor(bundle.getInt(TITLE_COLOR, defaultTextColor));

        ((CardView) mView.findViewById(R.id.cod_card)).setCardBackgroundColor
                (bundle.getInt(CARD_BACKGROUND_COLOR, defaultWhiteColor));

        mView.findViewById(R.id.cod_next_background).setBackgroundColor
                (bundle.getInt(NEXT_BACKGROUND_COLOR, defaultWhiteColor));
        TextView next = mView.findViewById(R.id.cod_next);
        next.setText(bundle.getString(NEXT_STRING));
        next.setTextColor(bundle.getInt(NEXT_TEXT_COLOR, defaultTextColor));
        next.setTextSize(bundle.getFloat(NEXT_SIZE, 14));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> permissions = new ArrayList<>();
                            for (PermissionItem item : mList) {
                                permissions.addAll(Arrays.asList(item.getPermissions()));
                            }
                            mCallback.onNextClick(permissions.toArray(new String[permissions.size()]));
                            dismiss();
                        }
                    }, 250);
                }
            }
        });

        PermissionAdapter adapter = new PermissionAdapter(mList,
                bundle.getInt(PERMISSION_NAME_COLOR, defaultTextColor),
                bundle.getFloat(PERMISSION_NAME_SIZE, 14));
        RecyclerView list = mView.findViewById(R.id.cod_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(list);
    }

    private void setPermissions(List<PermissionItem> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList = list;
    }

    public void setCallback(ConsentCallback callback) {
        mCallback = callback;
    }

    /**
     * Inner Interfaces
     */
    public interface ConsentCallback {
        void onNextClick(String[] permissions);
    }

}
