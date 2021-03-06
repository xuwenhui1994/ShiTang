package net.xuwenhui.shitang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import net.xuwenhui.core.ActionCallbackListener;
import net.xuwenhui.core.AppAction;
import net.xuwenhui.model.User;
import net.xuwenhui.shitang.R;
import net.xuwenhui.shitang.util.DensityUtils;
import net.xuwenhui.shitang.util.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 管理员：用户(包括普通用户和商家)适配器
 * <p/>
 * Created by xwh on 2016/5/5.
 */
public class UserForAdminAdapter extends CommonAdapter<User> {

	AppAction mAppAction;

	public UserForAdminAdapter(Context context, List<User> dataList, AppAction appAction) {
		super(context, dataList);
		mAppAction = appAction;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.item_user_for_admin, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		ViewHolder viewHolder = (ViewHolder) holder;

		final User user = mDataList.get(position);
		viewHolder.mTvId.setText(String.valueOf(user.getUser_id()));
		viewHolder.mTvPhoneNum.setText(user.getPhone_num());
		if (user.getImage_src().equals("")) {
			Picasso.with(mContext).load(R.mipmap.ic_launcher).into(viewHolder.mCircleImagePerson);
		} else {
			Picasso.with(mContext).load(user.getImage_src())
					.resize(DensityUtils.dp2px(mContext, 30), DensityUtils.dp2px(mContext, 30))
					.centerCrop().into(viewHolder.mCircleImagePerson);
		}

		// 根据用户角色改变布局样式
		if (user.getRole_id() == 2 || user.is_valid()) {
			viewHolder.mLayoutHandle.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.mLayoutHandle.setVisibility(View.VISIBLE);
			// 通过
			viewHolder.mBtnAgree.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					new MaterialDialog.Builder(mContext)
							.content("确定要通过吗？")
							.negativeText(R.string.disagree)
							.positiveText(R.string.agree)
							.onPositive(new MaterialDialog.SingleButtonCallback() {
								@Override
								public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
									mAppAction.user_check_merchant(user.getUser_id(), true, new ActionCallbackListener<Void>() {
										@Override
										public void onSuccess(Void data) {
											notifyItemRemoved(position);
											mDataList.remove(position);
											notifyItemRangeChanged(position, getItemCount());
										}

										@Override
										public void onFailure(String errorCode, String errorMessage) {
											T.show(mContext, errorMessage);
										}
									});

								}
							}).show();
				}
			});
			// 拒绝
			viewHolder.mBtnDisagree.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					new MaterialDialog.Builder(mContext)
							.content("确定要拒绝吗？")
							.negativeText(R.string.disagree)
							.positiveText(R.string.agree)
							.onPositive(new MaterialDialog.SingleButtonCallback() {
								@Override
								public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
									mAppAction.user_check_merchant(user.getUser_id(), false, new ActionCallbackListener<Void>() {
										@Override
										public void onSuccess(Void data) {
											notifyItemRemoved(position);
											mDataList.remove(position);
											notifyItemRangeChanged(position, getItemCount());
										}

										@Override
										public void onFailure(String errorCode, String errorMessage) {
											T.show(mContext, errorMessage);
										}
									});
								}
							}).show();
				}
			});
		}

	}

	class ViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.btn_disagree)
		Button mBtnDisagree;
		@Bind(R.id.btn_agree)
		Button mBtnAgree;
		@Bind(R.id.layout_handle)
		LinearLayout mLayoutHandle;
		@Bind(R.id.tv_id)
		TextView mTvId;
		@Bind(R.id.circle_image_person)
		CircleImageView mCircleImagePerson;
		@Bind(R.id.tv_phone_num)
		TextView mTvPhoneNum;
		@Bind(R.id.card_evaluation)
		CardView mCardEvaluation;

		ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}
