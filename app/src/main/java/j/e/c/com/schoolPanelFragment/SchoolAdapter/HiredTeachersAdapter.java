package j.e.c.com.schoolPanelFragment.SchoolAdapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.commonFragments.PlayVideoFragment;
import j.e.c.com.teacherPanelFragments.JobFormFragment;

public class HiredTeachersAdapter extends RecyclerView.Adapter<HiredTeachersAdapter.ViewHolder> {

    private ArrayList<Teacher> teacherArrayList;
    private Fragment context;

    public HiredTeachersAdapter(ArrayList<Teacher> teacherArrayList, Fragment context) {
        this.teacherArrayList = teacherArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_hired_teacher, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Teacher teacher = teacherArrayList.get(i);
        holder.location.setText("" + teacher.getCountry());
        try {
            Picasso.get().load(teacher.getBpic()).fit().into(holder.image);

        } catch (Exception e) {
            Picasso.get().load(R.drawable.khabib).into(holder.image);
        }
        holder.gender.setText("" + teacher.getGender());
        holder.salary.setText("" + teacher.getSalary());
        holder.fullTime.setText("" + teacher.getJobtitle());
        holder.nativ.setText("" + teacher.getJobtitle());

        holder.form.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "" + i, Toast.LENGTH_LONG).show();
            Helper.setTeacher(teacherArrayList.get(i));

            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(context, new JobFormFragment());
        });

        holder.videoIcon.setOnClickListener(v -> {

            Helper.setTeacher(teacherArrayList.get(i));

            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(context, new PlayVideoFragment());

        });
    }

    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.videoIcon)
        ImageView videoIcon;
        @BindView(R.id.nativ)
        TextView nativ;
        @BindView(R.id.salary)
        TextView salary;
        @BindView(R.id.fullTime)
        TextView fullTime;
        @BindView(R.id.gender)
        TextView gender;
        @BindView(R.id.location)
        TextView location;

        @BindView(R.id.image)
        CircularImageView image;
        @BindView(R.id.form)
        LinearLayout form;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
