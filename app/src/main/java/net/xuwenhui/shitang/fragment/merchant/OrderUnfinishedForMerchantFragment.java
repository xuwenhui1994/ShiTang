package net.xuwenhui.shitang.fragment.merchant;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.xuwenhui.model.Order;
import net.xuwenhui.model.OrderItem;
import net.xuwenhui.shitang.R;
import net.xuwenhui.shitang.adapter.OrderUnfinishedForMerchantAdapter;
import net.xuwenhui.shitang.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 商家：订单未完成界面
 * <p/>
 * Created by xwh on 2016/5/4.
 */
public class OrderUnfinishedForMerchantFragment extends BaseFragment {

	@Bind(R.id.list_order_unfinished)
	RecyclerView mListOrderUnfinished;

	OrderUnfinishedForMerchantAdapter mOrderUnfinishedForMerchantAdapter;

	@Override
	protected int getContentLayoutId() {
		return R.layout.fragment_order_unfinished;
	}

	@Override
	protected void initData() {
		// 初始化订单未完成列表
		mListOrderUnfinished.setLayoutManager(new LinearLayoutManager(mContext));
		mListOrderUnfinished.setItemAnimator(new DefaultItemAnimator());
		List<Order> data = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			OrderItem orderItem = new OrderItem(1, "测试菜品", 10.0f, i);
			List<OrderItem> orderItemList = new ArrayList<>();
			orderItemList.add(orderItem);
			Order order = new Order(i, (i + 1) / 2, "", "测试订单" + i, "2016-05-02 15:20:45", false, 20.0f, "许文辉 先生 1899562914\n海虹公寓4栋", "无", orderItemList);
			data.add(order);
		}
		mOrderUnfinishedForMerchantAdapter = new OrderUnfinishedForMerchantAdapter(mContext, data);
		mListOrderUnfinished.setAdapter(mOrderUnfinishedForMerchantAdapter);
	}

	@Override
	protected void initListener() {

	}

}
