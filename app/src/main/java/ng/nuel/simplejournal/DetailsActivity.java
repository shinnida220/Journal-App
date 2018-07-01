package ng.nuel.simplejournal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ng.nuel.simplejournal.data.DiaryEntry;
import ng.nuel.simplejournal.data.FirebaseDBHelper;

import static ng.nuel.simplejournal.MainActivity.EXTRA_DIARY_ENTRY_ID;

public class DetailsActivity extends AppCompatActivity {

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    private int mDiaryEntryId = DEFAULT_TASK_ID;

    FirebaseDBHelper firebaseDBHelper;
    DatabaseReference database;

    TextView tvTitle, tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvTitle = findViewById(R.id.textDiaryEntryTitle);
        tvContent = findViewById(R.id.textDiaryEntryContent);

        //Setup FB
        database = FirebaseDatabase.getInstance().getReference();
        // Our FB helper..
        firebaseDBHelper = new FirebaseDBHelper(database, this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DIARY_ENTRY_ID)) {
            if (mDiaryEntryId == DEFAULT_TASK_ID) {

                mDiaryEntryId = intent.getIntExtra(EXTRA_DIARY_ENTRY_ID, DEFAULT_TASK_ID);
                database.orderByChild(getResources().getString(R.string.FB_DB_KEY) )
                    .equalTo(mDiaryEntryId)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            DiaryEntry diaryEntry = dataSnapshot.getValue(DiaryEntry.class);
                            tvTitle.setText(diaryEntry.getTitle());
                            tvContent.setText(diaryEntry.getContent());
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            DiaryEntry diaryEntry = dataSnapshot.getValue(DiaryEntry.class);
                            tvTitle.setText(diaryEntry.getTitle());
                            tvContent.setText(diaryEntry.getContent());
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent editIntent = new Intent(DetailsActivity.this, AddEditDiaryActivity.class);
        startActivity(editIntent);
        return super.onOptionsItemSelected(item);
    }
}
