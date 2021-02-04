package j.e.c.com.teacherPanelFragments.TeacherAdapters;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.chatFragments.ChatFragment;
import j.e.c.com.commonFragments.NotificationFragment;
import j.e.c.com.commonFragments.ScheduledInterviewsFragment;
import j.e.c.com.schoolPanelFragment.HireFormTwoFragment;

public class teacherAppliedJobAdapter extends RecyclerView.Adapter<teacherAppliedJobAdapter.HolderJobs>  {
    private Fragment currentFragment;
    private ArrayList<School> schoolArrayList;
    String tid;
    SQLiteHandler sqLiteHandler;
    ProgressDialog progressDialog;
    private boolean isTeacherAppplayOnJobs;

    public teacherAppliedJobAdapter(Fragment fragment, ArrayList<School> schoolArrayList) {
        this.currentFragment = fragment;
        this.schoolArrayList = schoolArrayList;
    }

    @NonNull
    @Override
    public HolderJobs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentFragment.getContext()).inflate(R.layout.row_jobs, parent, false);
        fetchSchoolId();
        return new teacherAppliedJobAdapter.HolderJobs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderJobs holder, int position) {
        School school = schoolArrayList.get(position);

        // isTeacherAlreadyApplyOnJob(school);
        //jid= school.getId();
        holder.nativ.setText("" + school.getDemand());
        holder.sallary.setText("" + school.getSalary());
        holder.time.setText("" + school.getJobTitle());
        holder.location.setText("" + school.getSchoolLocation());

        holder.form.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_LONG).show();
            Helper.setSchool(schoolArrayList.get(position));
            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(currentFragment, new HireFormTwoFragment());

        });

        switch (school.getStatus()){
            case "0":
                holder.btnApplay.setText(R.string.pending);
                holder.btnApplay.setBackgroundResource(R.drawable.rounded_button);
                break;
            case "1":
                holder.btnApplay.setText(R.string.schedule);
                holder.btnApplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.fragmentTransaction(currentFragment, new ScheduledInterviewsFragment(school.getId()));
                    }
                });
                break;
            case "2":
                holder.btnApplay.setText(R.string.rejected);
                holder.btnApplay.setBackgroundResource(R.drawable.rounded_button);
                break;
            case "3":
                holder.btnApplay.setText(R.string.schedule);
                holder.btnApplay.setOnClickListener(v -> Helper.fragmentTransaction(currentFragment, new ScheduledInterviewsFragment(school.getId())));
                holder.chat.setVisibility(View.VISIBLE);
                holder.chat.setText("Inteview Link");
                holder.chat.setOnClickListener(v -> Helper.fragmentTransaction(currentFragment,new NotificationFragment()));
                break;
           /* case "4":
                holder.btnApplay.setText("Accept");
                //holder.btnApplay.setOnClickListener(v -> Helper.fragmentTransaction(currentFragment, new ScheduledInterviewsFragment(school.getId())));
                holder.chat.setVisibility(View.VISIBLE);
                holder.chat.setText("");
                holder.chat.setOnClickListener(v -> Helper.fragmentTransaction(currentFragment,new NotificationFragment()));
                break;*/
            case "4":
                holder.btnApplay.setVisibility(View.GONE);
                holder.chat.setVisibility(View.VISIBLE);
                holder.chat.setOnClickListener(v -> Helper.fragmentTransaction(currentFragment, new ChatFragment()));
        }

    }

    @Override
    public int getItemCount() {
        return schoolArrayList.size();
    }

    private void fetchSchoolId() {
        sqLiteHandler = new SQLiteHandler(currentFragment.getContext());
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            tid = res.getString(3);
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }
    }

    public class HolderJobs extends RecyclerView.ViewHolder {
        @BindView(R.id.form)
        ImageView form;
        @BindView(R.id.nativ)
        TextView nativ;
        @BindView(R.id.sallary)
        TextView sallary;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.btnApplay)
        TextView btnApplay;
        @BindView(R.id.chat)
        TextView chat;

        @BindView(R.id.applied)
        LinearLayout applied;

        public HolderJobs(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
