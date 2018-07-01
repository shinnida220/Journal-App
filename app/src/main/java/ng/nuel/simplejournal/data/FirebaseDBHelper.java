package ng.nuel.simplejournal.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import ng.nuel.simplejournal.R;

/**
 * Basically, this class will do CRUD operations.
 * To persist data, found out I have to use setValue().
 * Prior to calling setValue() method, I will call push() to ensure data is appended and not replaced
 * We fill a simple arraylist.
 */


public class FirebaseDBHelper {

    private static final String TAG = FirebaseDBHelper.class.getSimpleName();

    Context mContext;
    DatabaseReference db;
    List<DiaryEntry> mDiaryEntries = new ArrayList<>();

    public FirebaseDBHelper(DatabaseReference db, Context context) {
        this.db = db;
        this.mContext = context;
    }

    // CREATE
    public boolean save(DiaryEntry diaryEntry){
        // Initialize this to false..
        boolean saved = false;

        if(diaryEntry != null){
            try{
                db.child( mContext.getResources().getString(R.string.FB_DB_KEY) ).child(diaryEntry.getId()).setValue(diaryEntry);
                saved = true;
            } catch (DatabaseException e) {
                Log.v(TAG, "Error Saving in FBHelper:- " + e.getMessage());
                // Dump the error trace let me see..
                e.printStackTrace();
            }
        }

        return saved;
    }

    //READ
    public List<DiaryEntry> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return mDiaryEntries;
    }


    // The actual method that reads the data from Firebase..
    private void fetchData(DataSnapshot dataSnapshot)
    {

        if( null != mDiaryEntries)
           mDiaryEntries.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Object obj = ds.getValue();
            Log.v(TAG, obj.toString());
            DiaryEntry diaryEntry = ds.getValue(DiaryEntry.class);
            mDiaryEntries.add ( diaryEntry );
        }
    }

    public boolean delete(DiaryEntry diaryEntry){
        boolean deleted = false;
        try{
            db.child( mContext.getResources().getString(R.string.FB_DB_KEY) ).child(diaryEntry.getId()).setValue(null);
            deleted = true;
        }catch(DatabaseException e){
            Log.v(TAG, "Error deleting from DB in FBHelper:- " + e.getMessage());
            // Dump the error trace let me see..
            e.printStackTrace();
        }

        return deleted;
    }

}
