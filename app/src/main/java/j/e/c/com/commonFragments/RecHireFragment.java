package j.e.c.com.commonFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
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
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.DummyTeacherFragment;
import j.e.c.com.schoolPanelFragment.HireFormOneFragment;
import j.e.c.com.schoolPanelFragment.HireFormTwoFragment;
import j.e.c.com.schoolPanelFragment.HireTeacher;
import j.e.c.com.schoolPanelFragment.HiredTeachersFragment;
import j.e.c.com.schoolPanelFragment.ReceivedOffers;
import j.e.c.com.schoolPanelFragment.schoolPostedJobFragment;
import j.e.c.com.schoolPanelFragment.schoolShowingTeachersFragment;

public class RecHireFragment extends Fragment {

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rec_hire, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick({R.id.backArrow, R.id.hire, R.id.post_application, R.id.apply_for_job, R.id.hired, R.id.schedule, R.id.receivdOffers})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.hire:
                //isSchoolPositionExsisit();
               // Helper.fragmentTransaction(this,new schoolShowingTeachersFragment());

                if (Helper.getSchool().getStatus()!= null) {

                    if (Helper.getSchool().getStatus().equals("1")){
                        Helper.fragmentTransaction(this, new schoolShowingTeachersFragment());
                    }else{
                        Helper.fragmentTransaction(this, new WelcomeFragment());
                    }

                } else {
                    Helper.fragmentTransaction(this, new DummyTeacherFragment());

                }

                break;

            case R.id.receivdOffers:
                Helper.fragmentTransaction(this, new ReceivedOffers());
                break;

            case R.id.post_application:
                Helper.fragmentTransaction(this,new schoolPostedJobFragment());
                break;
            case R.id.apply_for_job:
                if (Helper.getSchool()!= null) {

                    if (Helper.getSchool().getStatus().equals("1")){
                        Helper.fragmentTransaction(this, new HireFormTwoFragment());
                    }else{
                        Helper.fragmentTransaction(this, new WelcomeFragment());
                    }

                } else {
                    Helper.fragmentTransaction(this, new HireFormOneFragment());

                }
              //  Helper.fragmentTransaction(this,new HireFormOneFragment());

                break;
            case R.id.hired:
                Helper.fragmentTransaction(this, new HiredTeachersFragment());
                break;
          /*  case R.id.schedule:
                Helper.fragmentTransaction(this,new ScheduledInterviewsFragment());
                break;*/
        }
    }

}
