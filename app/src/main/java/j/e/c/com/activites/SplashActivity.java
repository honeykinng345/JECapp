package j.e.c.com.activites;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import j.e.c.com.MainActivity;
import j.e.c.com.R;


public class SplashActivity extends AppCompatActivity {

    private ImageView logoSplash;
    private TextView companyName,tv1,tv2;

    private Animation anim1, anim2, anim3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setContentView(R.layout.activity_splash);
        init();
        logoSplash.setAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                logoSplash.setAnimation(anim2);
                companyName.setAnimation(anim3);

                companyName.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);

                anim3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }, 1500);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void init() {

        logoSplash = findViewById(R.id.ivLogoSplash);

        companyName = findViewById(R.id.compnayName);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        anim1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.scale);
        anim2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
        anim3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
    }

}
