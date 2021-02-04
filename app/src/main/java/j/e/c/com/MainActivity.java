package j.e.c.com;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.ButterKnife;
import j.e.c.com.Others.Helper;
import j.e.c.com.chatFragments.ChatFragment;
import j.e.c.com.commonFragments.FavoriteFragment;
import j.e.c.com.teacherPanelFragments.HomeFragment;
import j.e.c.com.teacherPanelFragments.ProfileFragment;
import j.e.c.com.teacherPanelFragments.RegistrationFragment;
import j.e.c.com.teacherPanelFragments.TeacherShowingJobsFragment;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        sessionManager = new SessionManager(getApplicationContext());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeacherShowingJobsFragment()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.navigation_person:
                    if (sessionManager.isLoggedIn()){
                       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                        openFragment(new ProfileFragment());
                        return true;
                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegistrationFragment()).addToBackStack(null).commit();

                        return true;
                    }


                case R.id.navigation_home:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                    openFragment(new HomeFragment());
                    return true;
                case R.id.navigation_chat:
                    openFragment(new ChatFragment());
                    return true;
                case R.id.navigation_like:
                    openFragment(new FavoriteFragment());
                    return true;
            }

            return true;
        });
    }
    private void openFragment(Fragment fragment){
        if(!fragment.isVisible()) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}