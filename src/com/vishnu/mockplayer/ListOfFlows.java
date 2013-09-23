package com.vishnu.mockplayer;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListOfFlows extends ListActivity {

    DatabaseHandler db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_player);
        db = new DatabaseHandler(getApplicationContext());
        populateList("");
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListOfFlows.this, MockPlayer.class);
                intent.putExtra("mock_id", (int) id);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(queryListener);
        return true;
    }

    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            populateList(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            populateList(query);
            return false;
        }
    };

    public void populateList(String query) {
        String[] columns = new String[] {"name", "timestamp"};
        int [] to = new int[] {R.id.name, R.id.time};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.mock_list_item, db.queryForMocks(query), columns, to);
        setListAdapter(adapter);
        db.close();
    }
}