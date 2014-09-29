package mn.ismartdev.lovehoroscope;

import android.widget.AbsListView;

public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight);
	String getDesc();
	void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
