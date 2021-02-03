package j.e.c.com.commonFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.SessionManager;
import j.e.c.com.activites.LoginActivity;
import j.e.c.com.teacherPanelFragments.HomeFragment;

public class SettingsFragment extends Fragment {

    SessionManager sessionManager;
    SQLiteHandler sqLiteHandler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        sqLiteHandler = new SQLiteHandler(getActivity());
        sessionManager = new SessionManager(getContext());

        return view;
    }

    @OnClick({R.id.backArrow, R.id.logoutBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.logoutBtn:
                sessionManager.setLogin(false);
                sqLiteHandler.removeAll();
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
        }
    }
}
