package com.vishnu.mockplayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.vishnu.mockplayer.utilities.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListOfFlows extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_player);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String[] columns = new String[] {"name", "timestamp"};
        int [] to = new int[] {R.id.name, R.id.time};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.mock_list_item, db.selectAllMocks(), columns, to);
        setListAdapter(adapter);
        db.close();
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
}