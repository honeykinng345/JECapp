package j.e.c.com.teacherPanelFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.R;
import j.e.c.com.activites.LoginActivity;
import j.e.c.com.activites.RegisterActivity;

public class RegistrationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.registerBtn, R.id.LoginBtn, R.id.backArrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.LoginBtn:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
