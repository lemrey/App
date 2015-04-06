package lemrey.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import lemrey.com.app.R;
import lemrey.com.app.adapter.RuleAdapter;
import lemrey.com.app.rule.RuleBook;


public class RuleManagerActivity extends ActionBarActivity {

	private RuleAdapter mRuleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_manager);

		final ExpandableListView listRules = (ExpandableListView) findViewById(R.id.listRules);
		mRuleAdapter = new RuleAdapter(this, RuleBook.rules());
		listRules.setAdapter(mRuleAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		mRuleAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_rule_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id) {
			case R.id.menu_add:
				onButtonNewRuleClicked(null);
		}

		return super.onOptionsItemSelected(item);
	}

	public void onButtonNewRuleClicked(View view) {
		Intent intent  = new Intent(this, RuleCreatorActivity.class);
		startActivity(intent);
	}
}
