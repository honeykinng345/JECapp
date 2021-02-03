package j.e.c.com.commonFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.teacherPanelFragments.AppliedJobsFragment;
import j.e.c.com.teacherPanelFragments.TeacherShowingJobsFragment;
import j.e.c.com.teacherPanelFragments.YourApplicationFragment;

public class RecApplyFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rec_apply, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.backArrow, R.id.your_application, R.id.apply_for_job, R.id.applied, R.id.schedule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.your_application:
                Helper.fragmentTransaction(this, new YourApplicationFragment());
                break;
            case R.id.apply_for_job:
                if (Helper.getTeacher().getStatus()!=null) {
                    if (Helper.getTeacher().getStatus().equals("1")){
                        Helper.fragmentTransaction(this, new TeacherShowingJobsFragment());
                    }else{
                        Helper.fragmentTransaction(this, new WelcomeFragment());
                    }

                } else {
                    Helper.fragmentTransaction(this, new DummyListingFragment());
                }


                break;
            case R.id.applied:
                Helper.fragmentTransaction(this, new AppliedJobsFragment());
                break;
           /* case R.id.schedule:
                Helper.fragmentTransaction(this, new ScheduledInterviewsFragment());
                break;*/
        }
    }



}
