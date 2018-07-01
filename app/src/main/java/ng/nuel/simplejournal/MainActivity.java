package ng.nuel.simplejournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ng.nuel.simplejournal.data.DiaryEntry;
import ng.nuel.simplejournal.data.DiaryEntryAdapter;
import ng.nuel.simplejournal.data.FirebaseDBHelper;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity{
        //implements DiaryEntryAdapter.ItemClickListener {

    RecyclerView mRecyclerViewDiaryEntries;
    DiaryEntryAdapter mDiaryEntryAdapter;

    FirebaseDBHelper firebaseDBHelper;
    DatabaseReference database;

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_DIARY_ENTRY_ID = "extraDiaryEntryId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Setup FB
        database = FirebaseDatabase.getInstance().getReference();

        // Our FB helper..
        firebaseDBHelper = new FirebaseDBHelper(database, this);

        // TODO 1. sETUP RECYCLER VIEW
        mRecyclerViewDiaryEntries = findViewById(R.id.recyclerViewDiaryEntries);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerViewDiaryEntries.setLayoutManager(new LinearLayoutManager(this));

        // We Initialize the adapter and attach it to the RecyclerView
        mDiaryEntryAdapter = new DiaryEntryAdapter(this, firebaseDBHelper.retrieve(), new DiaryEntryAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(String itemId) {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra(MainActivity.EXTRA_DIARY_ENTRY_ID, itemId);
                startActivity(i);
            }
        });
        mRecyclerViewDiaryEntries.setAdapter(mDiaryEntryAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerViewDiaryEntries.addItemDecoration(decoration);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // TODO Implement deleting of task...
                // Get the position...
                int position = viewHolder.getAdapterPosition();
                // Get the item to be deleted
                DiaryEntry diaryEntry = mDiaryEntryAdapter.getDiaryEntry(position);
                // Call the FBHelper to do the deleting..
                if (firebaseDBHelper.delete(diaryEntry) ){
                    mDiaryEntryAdapter.setDiaryEntries(firebaseDBHelper.retrieve());
                    mDiaryEntryAdapter.notifyDataSetChanged();
                }
            }
        }).attachToRecyclerView(mRecyclerViewDiaryEntries);

        // TODO 2. Create activity to save new journal entries..
        // TODO 3. Show these entries on a list
        // TODO 4. EDIT these entries
        // TODO 5. Delete these entries. b. ADD Confirmation to delete
        //

        // Setup our add button..
        FloatingActionButton fab = findViewById(R.id.fabAddEntry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            startActivity(new Intent(MainActivity.this, AddEditDiaryActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mDiaryEntryAdapter != null){
            mDiaryEntryAdapter.setDiaryEntries(firebaseDBHelper.retrieve());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( mDiaryEntryAdapter != null){
            mDiaryEntryAdapter.setDiaryEntries(firebaseDBHelper.retrieve());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Here we inflate our menu ..
        // AT, it has just the logout menu..
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the item that was clicked..
        int clickedItemId = item.getItemId();
        if (clickedItemId == R.id.menu_menu_signout) {
            FirebaseAuth.getInstance().signOut();

            //Load the login screen
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);

            // Close this activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onItemClickListener(String itemId) {
//        Toast.makeText(getApplicationContext(),  itemId, Toast.LENGTH_SHORT).show();
//
//        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
//        detailsIntent.putExtra(MainActivity.EXTRA_DIARY_ENTRY_ID, itemId);
//        startActivity(detailsIntent);
//    }
}
