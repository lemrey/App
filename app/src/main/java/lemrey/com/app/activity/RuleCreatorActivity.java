package lemrey.com.app.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import lemrey.com.app.DeviceRegister;
import lemrey.com.app.R;
import lemrey.com.app.adapter.DeviceFeatureAdapter;
import lemrey.com.app.device.Device;
import lemrey.com.app.device.Feature;
import lemrey.com.app.device.FeatureParam;
import lemrey.com.app.rule.Rule;
import lemrey.com.app.rule.RuleBook;

public class RuleCreatorActivity extends ActionBarActivity implements
		View.OnClickListener, ListClickCallback {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private DeviceFeatureAdapter mDeviceFeatureAdapter;
	private Button mButtonFinish;
	private int count = 0;
	private Feature event;
	private String srcAddr;
	private Feature cmd;
	private String destAddr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_creator);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mButtonFinish = (Button) findViewById(R.id.buttonFinish);
		mButtonFinish.setEnabled(false);
		mButtonFinish.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_rule_creator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View view) {
		Rule rule = new Rule(srcAddr, event, destAddr, cmd);
		RuleBook.addRule(rule);
		Log.d("rulezz", "rule created! " + srcAddr + event.name + destAddr + cmd.name);
		finish();
	}

	@Override
	public void onItemClicked(int item, int id) {
		Log.d("Callback", "you clicked " + item + " id " + id);
		final Device dev = DeviceRegister.get(item);
		if (count == 0) {
			count++;
			PagerTabStrip tabStrip = (PagerTabStrip) mViewPager.findViewById(R.id.pager_tab_strip);
			tabStrip.setBackgroundColor(Color.rgb(240, 100, 100));

			srcAddr = dev.mAddress;
			event = dev.events().get(id);
			TextView labelEvent = (TextView) findViewById(R.id.labelEvent);
			labelEvent.setText(event.name);

			mSectionsPagerAdapter.changeAdapters(event.paramType);

		} else {
			destAddr = dev.mAddress;
			cmd = mSectionsPagerAdapter.mFragments.get(item).mFeaturesShown.get(id);

			TextView labelEvent = (TextView) findViewById(R.id.labelCommand);
			labelEvent.setText(cmd.name);

			mButtonFinish.setEnabled(true);
			mButtonFinish.setBackgroundColor(Color.rgb(155, 225, 180));
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public ArrayList<Feature> mFeaturesShown;
		private int mDeviceIndex;
		private ListClickCallback callback;
		private ListView mListView;

		public PlaceholderFragment() {
			;
		}

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.mDeviceIndex = sectionNumber;
			fragment.mFeaturesShown = new ArrayList<>(
					DeviceRegister.eventsForDeviceId(sectionNumber));
			return fragment;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			callback = (ListClickCallback) activity;
		}

		public void changeAdapter(FeatureParam filter) {
			mFeaturesShown.clear();
			for (Feature ft : DeviceRegister.cmdsForDeviceId(mDeviceIndex)) {
				if (ft.paramType.equals(filter)) mFeaturesShown.add(ft);
			}

			mListView.setAdapter(new DeviceFeatureAdapter(getActivity(),
					R.layout.list_objects_entry2, mFeaturesShown));
		}


		@Override
		public View onCreateView(LayoutInflater inflater, final ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_rule_creator, container, false);
			//Log.d("Fragment", "OnCreateView " + mDeviceIndex + " adapter " + adapter);
			mListView = (ListView) rootView.findViewById(R.id.listView);
			mListView.setAdapter(new DeviceFeatureAdapter(getActivity(),
					R.layout.list_objects_entry2, DeviceRegister.eventsForDeviceId(mDeviceIndex)
			));

			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					callback.onItemClicked(mDeviceIndex, i);
				}
			});

			return rootView;
		}
	}


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		ArrayList<PlaceholderFragment> mFragments = new ArrayList<>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void changeAdapters(FeatureParam filter) {
			for (PlaceholderFragment fragment : mFragments)
				fragment.changeAdapter(filter);
		}

		@Override
		public Fragment getItem(int position) {
			Log.d("Pager", "get item " + position);
			PlaceholderFragment fragment = PlaceholderFragment.newInstance(position);
			mFragments.add(fragment);
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			return fragment;
		}

		@Override
		public int getCount() {
			return DeviceRegister.devices().size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (DeviceRegister.devices().size() != 0)
				return DeviceRegister.get(position).name();
			else
				return "no";
		}
	}

}
