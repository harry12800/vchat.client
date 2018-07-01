package cn.harry12800.vchat.adapter;

/**
 * Created by harry12800 on 17-5-30.
 */
public abstract class BaseAdapter<T extends ViewHolder> {
	public int getCount() {
		return 0;
	}

	public abstract T onCreateViewHolder(int viewType);

	public HeaderViewHolder onCreateHeaderViewHolder(int viewType, int position) {
		return null;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public abstract void onBindViewHolder(T viewHolder, int position);

	public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int position) {

	}

}
