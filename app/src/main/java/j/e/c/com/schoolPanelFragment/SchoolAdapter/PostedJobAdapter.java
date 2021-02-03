package j.e.c.com.schoolPanelFragment.SchoolAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.schoolPanelFragment.HireFormTwoFragment;
import j.e.c.com.schoolPanelFragment.HireTeacher;
import j.e.c.com.teacherPanelFragments.TeacherShowingJobsFragment;

public class PostedJobAdapter extends RecyclerView.Adapter<PostedJobAdapter.ViewHolder> {



    private Fragment currentFragment;
    private ArrayList<School> schoolArrayList;

    public PostedJobAdapter(Fragment currentFragment, ArrayList<School> schoolArrayList) {
        this.currentFragment = currentFragment;
        this.schoolArrayList = schoolArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(currentFragment.getContext()).inflate(R.layout.item_posted_job, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        School school = schoolArrayList.get(i);
        //jid= school.getId();
        viewHolder.nativ.setText("" + school.getCity());
        viewHolder.sallary.setText("" + school.getSalary());
        viewHolder.time.setText("" + school.getJobTitle());
        viewHolder.location.setText("" + school.getSchoolLocation());

        if (school.getStatus().equals("0")) {
            viewHolder.Inactive.setVisibility(View.VISIBLE);

        } else {
            viewHolder.activate.setVisibility(View.VISIBLE);
            viewHolder.kk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.setSchool(schoolArrayList.get(i));
                    Helper.fragmentTransaction(currentFragment, new HireTeacher());
                }
            });
        }

        viewHolder.form.setOnClickListener(v -> {
            // Toast.makeText(v.getContext(), ""+pos, Toast.LENGTH_LONG).show();
            Helper.setSchool(schoolArrayList.get(i));

            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(currentFragment, new HireFormTwoFragment());
        });
    }

    @Override
    public int getItemCount() {
        return schoolArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.Inactive)
        TextView Inactive;
        @BindView(R.id.activate)
        LinearLayout activate;
        @BindView(R.id.kk)
        TextView kk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
