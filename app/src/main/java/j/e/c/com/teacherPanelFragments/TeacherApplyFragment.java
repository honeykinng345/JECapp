package j.e.c.com.teacherPanelFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.R;

public class TeacherApplyFragment extends Fragment {


   // @BindView(R.id.agentSpinner)
    //MaterialSpinner agentSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_apply, container, false);
       // ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        updateSpinners();
    }

    void updateSpinners() {
        String[] agentArray = {"Agent Reference", "JEC1", "JEC2"};
        //agentSpinner.setItems(agentArray);
        //agentSpinner.setSelectedIndex(0);
    }
}
