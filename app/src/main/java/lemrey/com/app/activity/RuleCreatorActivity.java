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
import lemrey.com.app.adapter.DeviceCmdAdapter;
import lemrey.com.app.adapter.DeviceFeatureAdapter;
import lemrey.com.app.device.Command;
import lemrey.com.app.device.Device;
import lemrey.com.app.device.Event;

public class RuleCreatorActivity extends ActionBarActivity implements
		ListClickCallback {

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
	private Button mButtonFinish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_creator);

		Device d = new Device("cool lamp","00");
		ArrayList<Event> e = new ArrayList<>();
		e.add(new Event("light low"));
		d.addEvents(e);
		ArrayList<Command> c =new ArrayList<>();
		c.add(new Command("light on"));
		d.addCommands(c);
		DeviceRegister.addDevice(d);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mButtonFinish = (Button) findViewById(R.id.buttonFinish);
		mButtonFinish.setEnabled(false);

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

	private int count = 0;

	@Override
	public void onItemClicked(int item, int id) {
		Log.d("Callback", "you clicked " + item + "id " + id);
		if (count == 0) {
			count++;
			PagerTabStrip tabStrip = (PagerTabStrip)mViewPager.findViewById(R.id.pager_tab_strip);
			tabStrip.setBackgroundColor(Color.rgb(240, 100, 100));

			TextView labelEvent = (TextView)findViewById(R.id.labelEvent);
			labelEvent.setText(DeviceRegister.devices().get(item).events().get(id).name());

			mSectionsPagerAdapter.changeAdapters();


		} else
		{
			TextView labelEvent = (TextView)findViewById(R.id.labelCommand);
			labelEvent.setText(DeviceRegister.devices().get(item).commands().get(id).name());

			mButtonFinish.setEnabled(true);
			mButtonFinish.setBackgroundColor(Color.rgb(155,225,180));
		}

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		//private int adapter;
		private int mDevicePos;
		private ListClickCallback callback;
		private View rootView;
		private ListView listView;

		public PlaceholderFragment() {
		}

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.mDevicePos = sectionNumber;
			//fragment.adapter = adapter;
			return fragment;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			callback = (ListClickCallback) activity;
		}

		public void changeAdapter() {
			//adapter = 1;

				listView.setAdapter(new DeviceCmdAdapter(getActivity(),
						R.layout.list_objects_entry2, DeviceRegister.devices().get(mDevicePos).commands()));
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_rule_creator, container, false);

			//Log.d("Fragment", "OnCreateView " + mDevicePos + " adapter " + adapter);
			listView = (ListView) rootView.findViewById(R.id.listView);

			listView.setAdapter(new DeviceFeatureAdapter(getActivity(),
					R.layout.list_objects_entry2, DeviceRegister.devices().get(mDevicePos).events()));

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					callback.onItemClicked(mDevicePos, i);
				}
			});

			return rootView;
		}
	}


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		ArrayList<PlaceholderFragment> mFragments = new ArrayList<>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void changeAdapters() {
			for (PlaceholderFragment fragment : mFragments)
				fragment.changeAdapter();
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
				return DeviceRegister.devices().get(position).name();
			else
				return "no";
		}
	}

}
