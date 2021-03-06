package net.xuwenhui.shitang.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.xuwenhui.core.AppAction;
import net.xuwenhui.shitang.AppApplication;

import butterknife.ButterKnife;

/**
 * Fragment抽象基类
 * <p/>
 * Created by xwh on 2016/3/24.
 */
public abstract class BaseFragment extends Fragment {

	/**
	 * 打印标签
	 */
	protected String TAG;
	/**
	 * 上下文实例
	 */
	protected Context mContext;
	/**
	 * 应用全局实例
	 */
	protected AppApplication mApplication;
	/**
	 * 核心层的Action实例
	 */
	protected AppAction mAppAction;

	/**
	 * 获取布局文件Id
	 *
	 * @return
	 */
	protected abstract int getContentLayoutId();

	/**
	 * 初始化数据
	 *
	 * @return
	 */
	protected abstract void initData();

	/**
	 * 初始化监听器
	 *
	 * @return
	 */
	protected abstract void initListener();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getContentLayoutId(), container, false);
		// 字段赋值
		TAG = getClass().getName();
		mContext = getActivity();
		mApplication = (AppApplication) getActivity().getApplication();
		mAppAction = mApplication.getAppAction();
		// 注解绑定
		ButterKnife.bind(this, view);
		initData();
		initListener();
		Log.i(TAG, "onCreateView");
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 绑定重置
		ButterKnife.unbind(this);
		Log.i(TAG, "onDestroyView");
	}
}
