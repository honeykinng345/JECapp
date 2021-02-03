package j.e.c.com.commonFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ncorti.slidetoact.SlideToActView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SessionManager;
import j.e.c.com.activites.LoginActivity;
import j.e.c.com.teacherPanelFragments.TeacherApplyFragment;

public class DummyListingFragment extends Fragment {
    @BindView(R.id.lock)
    SlideToActView lock;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dummy_listing, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        lock.setOnSlideCompleteListener(slideToActView -> {
            if (sessionManager.isLoggedIn()) {
               /* FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new TeacherApplyFragment()).addToBackStack(null).commit();*/
                Helper.fragmentTransaction(this, new TeacherApplyFragment(), false);
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getFragmentManager().popBackStack();

            }
        });
    }

    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        getFragmentManager().popBackStack();
    }
}
