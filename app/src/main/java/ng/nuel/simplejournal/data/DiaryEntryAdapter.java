package ng.nuel.simplejournal.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ng.nuel.simplejournal.R;

public class DiaryEntryAdapter extends RecyclerView.Adapter<DiaryEntryAdapter.DiaryEntryViewHolder>  {

    // Constant for date format
    private static final String DATE_FORMAT = "dd MMM";
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    // Member variable to handle item clicks
    final private DiaryEntryClickListener mItemClickListener;

    // Class variables for the List that holds task data and the Context
    private List<DiaryEntry> mDiaryEntries;
    private Context mContext;


    public DiaryEntryAdapter(Context context, List<DiaryEntry> diaryEntries, DiaryEntryClickListener itemClickListener){
        mContext = context;
        mItemClickListener = itemClickListener;
        mDiaryEntries = diaryEntries;
    }


    @Override
    public DiaryEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View diaryEntryRowView =
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.diary_entry_list_row, parent, false);
        return new DiaryEntryViewHolder(diaryEntryRowView);
    }

    @Override
    public void onBindViewHolder(DiaryEntryViewHolder holder, int position) {
        // Determine the values of the wanted data
        DiaryEntry diaryEntry = mDiaryEntries.get(position);
        String mCont = diaryEntry.getContent();
        String briefContent = mCont.substring(0, (mCont.length() > 50) ? 50 : mCont.length()-1);
        String title = diaryEntry.getTitle();
        String updatedAt = diaryEntry.getUpdatedAt();


        //Set values
        holder.date.setText(updatedAt);
        holder.title.setText(title);
        holder.desc.setText(briefContent);

        Log.d("OnBindViewHolder", title.substring(0,1));
        holder.icon.setText(title.substring(0,1));

        // TODO Generate and Use random colors
        // Random Color
        holder.icon.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        holder.icon.setTextColor(mContext.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return (mDiaryEntries != null) ? mDiaryEntries.size() : 0;
    }

    public List<DiaryEntry> getDiaryEntries(){
        return mDiaryEntries;
    }
    public DiaryEntry getDiaryEntry(int position){
        return mDiaryEntries.get(position);
    }

    /**
     * When data changes, this method updates the list of mDiaryEntries
     * and notifies the adapter to use the new values on it
     */
    public void setDiaryEntries(List<DiaryEntry> entries){
        mDiaryEntries = entries;
        notifyDataSetChanged();
    }


    // Viewholder for the DiaryEntryAdapter
    class DiaryEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView icon, title, date, desc;

        /**
         * Constructor for the DiaryEntryViewHolder.
         * @param itemView The view inflated in onCreateViewHolder
         */
        public DiaryEntryViewHolder (View itemView){
            super(itemView);
            icon = itemView.findViewById(R.id.textViewIcon);
            title = itemView.findViewById(R.id.textViewSubject);
            desc = itemView.findViewById(R.id.textViewContentBrief);
            date = itemView.findViewById(R.id.textViewDate);
        }

        @Override
        public void onClick(View view) {
            // TODO: Handle adapter item click..
            String elementId = mDiaryEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onDiaryEntryItemClickListener(elementId);
        }
    }


    // Interface to be implemented by listing activity..
    public interface DiaryEntryClickListener {
        void onDiaryEntryItemClickListener(String itemId);
    }
}
