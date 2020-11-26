package com.example.alcoholic.common;

import android.app.Application;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.example.alcoholic.R;
import com.example.alcoholic.db.DbMsgManager;
import com.example.alcoholic.db.DbUserManager;
import com.example.alcoholic.helper.ActivityStackManager;
import com.example.alcoholic.http.model.RequestHandler;
import com.example.alcoholic.http.server.ReleaseServer;
import com.example.alcoholic.other.AppConfig;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestServer;
import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import okhttp3.OkHttpClient;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 项目中的 Application 基类
 */
public final class MyApplication extends Application implements LifecycleOwner {

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        mApplication = this;
        initSdk(this);
        initDb();
    }

    public static Application getApplication() {
        return mApplication;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    /**
     * 初始化一些第三方框架
     */
    public static void initSdk(Application application) {
        // 吐司工具类
        ToastUtils.init(application);

        //logger
        Logger.addLogAdapter(new AndroidLogAdapter());

        //mmkv
        MMKV.initialize(application);

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });

        // 初始化标题栏全局样式
        TitleBar.initStyle(new TitleBarLightStyle(application) {

            @Override
            public Drawable getBackground() {
                return new ColorDrawable(ContextCompat.getColor(application, R.color.colorPrimary));
            }

            @Override
            public Drawable getBackIcon() {
                return getDrawable(R.drawable.arrows_left_ic);
            }
        });

        // 本地异常捕捉
//        CrashHandler.register(application);

        // 友盟统计、登录、分享 SDK
//        UmengClient.init(application);

        // Bugly 异常捕捉
//        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setEnableLastTime(false));
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20));

        // Activity 栈管理初始化
        ActivityStackManager.getInstance().init(application);

        // 网络请求框架初始化
        IRequestServer server;
        server = new ReleaseServer();

        EasyConfig.with(new OkHttpClient())
                // 是否打印日志
                .setLogEnabled(AppConfig.isDebug())
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求重试次数
                .setRetryCount(1)
                // 添加全局请求参数
                //.addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("time", "20191030")
                // 启用配置
                .into();

        // Activity 侧滑返回
//        SmartSwipeBack.activitySlidingBack(application, activity -> {
//            if (activity instanceof SwipeAction) {
//                return ((SwipeAction) activity).isSwipeEnable();
//            }
//            return true;
//        });


        //im
        int pid = android.os.Process.myPid();
        String processAppName = AppConfig.getAppName(application,pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (!(processAppName == null ||!processAppName.equalsIgnoreCase(application.getPackageName()))) {
            EMOptions options = new EMOptions();
            // 默认添加好友时，是不需要验证的，改成需要验证
            options.setAcceptInvitationAlways(false);
            //初始化
            EMClient.getInstance().init(application, options);
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
        }

    }


    public static void initDb(){
        DbMsgManager.getInstance();
        DbUserManager.getInstance();
    }
}