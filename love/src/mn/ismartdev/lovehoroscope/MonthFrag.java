package mn.ismartdev.lovehoroscope;

import java.sql.SQLException;

import mn.ismartdev.horoscope.model.DatabaseHelper;
import mn.ismartdev.horoscope.model.MonthZodiac;
import mn.ismartdev.horoscope.text.Regular;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class MonthFrag extends Fragment {

	private PagerSlidingTabStrip tab;
	private ViewPager pager;
	View v;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.month_main, container, false);
		tab = (PagerSlidingTabStrip) v.findViewById(R.id.monthNameTab);
		pager = (ViewPager) v.findViewById(R.id.monthPager);
		pager.setAdapter(new MonthPagerAdapter(getActivity()
				.getSupportFragmentManager()));
		tab.setUnderlineColorResource(android.R.color.white);
		tab.setViewPager(pager);
		return v;
	}

	public class MonthPagerAdapter extends FragmentStatePagerAdapter {

		private String[] months = { "1-р сар", "2-р сар", "3-р сар", "4-р сар",
				"5-р сар", "6-р сар", "7-р сар", "8-р сар", "9-р сар",
				"10-р сар", "11-р сар", "12-р сар" };

		public MonthPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return months[position];
		}

		@Override
		public int getCount() {
			return months.length;
		}

		@Override
		public Fragment getItem(int position) {
			return MonthItemFrag.newInstance(position);
		}
	}

	public static class MonthItemFrag extends Fragment {
		int mNum;

		View v;
		Regular text;
		DatabaseHelper helper;

		public static MonthItemFrag newInstance(int num) {

			MonthItemFrag f = new MonthItemFrag();
			Bundle args = new Bundle();
			args.putInt("num", num + 1);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			v = inflater.inflate(R.layout.gift_item, container, false);
			text = (Regular) v.findViewById(R.id.gift_text);
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			helper = new DatabaseHelper(getActivity());
			try {
				MonthZodiac zod = helper.getMonthZodiacDao()
						.queryForEq("id", mNum).get(0);
				text.setText(zod.text + "");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
