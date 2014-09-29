package mn.ismartdev.lovehoroscope;

import java.sql.SQLException;

import mn.ismartdev.horoscope.model.DatabaseHelper;
import mn.ismartdev.horoscope.model.Zodiac;
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

public class GiftFrag extends Fragment {
	private PagerSlidingTabStrip tab;
	private ViewPager pager;
	View v;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.gift_main, container, false);
		tab = (PagerSlidingTabStrip) v.findViewById(R.id.zodiacNameTab);
		pager = (ViewPager) v.findViewById(R.id.zodiacPager);
		pager.setAdapter(new PagerAdapter(getActivity()
				.getSupportFragmentManager()));
		tab.setUnderlineColorResource(android.R.color.white);
		tab.setViewPager(pager);
		return v;
	}

	public class PagerAdapter extends FragmentStatePagerAdapter {

		private String[] signs = { "Хонь", "Үхэр", "Ихэр", "Мэлхий", "Арслан",
				"Охин", "Жинлүүр", "Хилэнц", "Нум", "Матар", "Хумх", "Загас", };

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return signs[position];
		}

		@Override
		public int getCount() {
			return signs.length;
		}

		@Override
		public Fragment getItem(int position) {
			return GiftItemFrag.newInstance(position);
		}
	}

	public static class GiftItemFrag extends Fragment {
		int mNum;

		View v;
		Regular text;
		DatabaseHelper helper;

		public static GiftItemFrag newInstance(int num) {

			GiftItemFrag f = new GiftItemFrag();
			// Supply num input as an argument.
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
				Zodiac zod = helper.getZodiacDao().queryForEq("id", mNum)
						.get(0);
				text.setText(zod.gift + "");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
