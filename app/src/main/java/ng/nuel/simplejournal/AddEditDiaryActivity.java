package ng.nuel.simplejournal;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.UUID;

import ng.nuel.simplejournal.data.DiaryEntry;
import ng.nuel.simplejournal.data.FirebaseDBHelper;

public class AddEditDiaryActivity extends AppCompatActivity {

    EditText mTitle, mContent;
    FirebaseDBHelper firebaseDBHelper;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_diary);

        // Enable the back buton..
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup the input boxes..
        mTitle = findViewById(R.id.addEditDiaryEntryTitle);
        mContent = findViewById(R.id.addEditDiaryEntryContent);

        //Set up the save button
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            doSaveOperation();
            }
        });

        //Setup FB
        database = FirebaseDatabase.getInstance().getReference();
        firebaseDBHelper = new FirebaseDBHelper(database, this);
    }

    private void doSaveOperation(){

        // get the layout to use..
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
            // Set the message body
            .setMessage(getString(R.string.dialog_confirm_body))
            // Set the title
            .setTitle(getString(R.string.dialog_confirm_title))
            // make it a modal
            .setCancelable(false)
            // Set Ok button
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // TODO cALL FB instance and save...
                    DiaryEntry diaryEntry = new DiaryEntry();
                    diaryEntry.setTitle(mTitle.getText().toString());
                    diaryEntry.setContent(mContent.getText().toString());
                    diaryEntry.setId(UUID.randomUUID().toString());
                    String d = diaryEntry.dateToString(new Date(), "yyyy-mm-dd");
                    diaryEntry.setUpdatedAt(d);

                    if (firebaseDBHelper.save(diaryEntry)){
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.save_operation_successful), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.save_operation_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            })
            // Set cancel button
            .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
            );

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
