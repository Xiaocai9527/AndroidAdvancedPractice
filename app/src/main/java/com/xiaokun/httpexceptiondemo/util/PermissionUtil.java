package com.xiaokun.httpexceptiondemo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.R;


/**
 * Created by 肖坤 on 2017/9/27.
 */

public class PermissionUtil
{

    public static void showMissingPermissionDialog(final Activity activity, final String pers)
    {
        final View dialog = View.inflate(activity, R.layout.permission_dialog_layout, null);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        final LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.linear_layout);
        final ImageView img1 = (ImageView) dialog.findViewById(R.id.img_per1);
        final ImageView img2 = (ImageView) dialog.findViewById(R.id.img_per2);
        message.setText(Html.fromHtml("应用缺少必要的" +
                "<font color=\"#3a62ac\">" + pers + "</font>" +
                "权限。<br>点击<font color=\"#3a62ac\">设置按钮</font>后，按如下图所示操作，<font color=\"#3a62ac\">勾选未授权的选项</font>后按返回键回退到应用。"));
//        message.setText("应用缺少必要的" + pers + "权限。\n点击设置按钮后按如下图所示操作，勾选未授权的选项后按返回键回退到应用。");
        AlertDialog.Builder builder = dialogBuilder(activity, 0, dialog);
        Drawable drawable = activity.getResources().getDrawable(R.drawable.permission1);
        final int width = drawable.getIntrinsicWidth();
        final int height = drawable.getIntrinsicHeight();
        dialog.post(new Runnable()
        {
            @Override
            public void run()
            {
                //保证getWidth不为0
                int linearLayoutWidth = linearLayout.getWidth();
                LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams) img1.getLayoutParams();
                imgParams.width = linearLayoutWidth;
                imgParams.height = linearLayoutWidth * height / width;
                img1.setLayoutParams(imgParams);
                img2.setLayoutParams(imgParams);
                //判断手机类型来展示截图
            }
        });

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        Toast.makeText(App.getAppContext(), "当前应用缺少" + pers + "权限", Toast.LENGTH_SHORT).show();
//                        CustomDialogUtil.showSuccessTips(activity, "当前应用缺少" + pers + "权限");
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startAppSettings(activity);
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @param activity
     */
    public static void startAppSettings(Activity activity)
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, App.getSp().getInt(Constants.REQUEST_CODE_PERMISSION, 0));
    }

    public static AlertDialog.Builder dialogBuilder(Context context, int title, View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (view != null)
        {
            builder.setView(view);
        }
        if (title > 0)
        {
            builder.setTitle(title);
        }
        return builder;
    }
}
