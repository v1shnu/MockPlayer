package com.vishnu.mockplayer.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.dialogs.FlowNamingDialog;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

public class ListOfFlows extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_player);
        populateList("");
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playFlow(id);
            }
        });
        registerForContextMenu(listView);
    }

    public void createFlow(View view) {
        FlowNamingDialog flowNamingDialog = new FlowNamingDialog(this);
        flowNamingDialog.show(getFragmentManager(), "flowNamer");
    }

    private void playFlow(long id) {
        Intent intent = new Intent(ListOfFlows.this, MockPlayer.class);
        intent.putExtra("mockId", (int) id);
        startActivity(intent);
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
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String[] columns = new String[] {"name", "timestamp"};
        int [] to = new int[] {R.id.name, R.id.time};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.mock_list_item, db.queryForMocks(query), columns, to, 0);
        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        if(view.getId() == getListView().getId()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.play_flow:
                playFlow(info.id);
                return true;
            case R.id.delete_flow:
                deleteFlow(info.id);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteFlow(long id) {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.deleteMock((int)id);
        Utilities.displayToast(getApplicationContext(), "Flow has been deleted");
        populateList("");
    }
}