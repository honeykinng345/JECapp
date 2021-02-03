package j.e.c.com.commonFragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.Others.Helper;
import j.e.c.com.Others.Prefrence;
import j.e.c.com.R;

public class PlayVideoFragment extends Fragment {

    SimpleExoPlayer exoPlayer;

    @BindView(R.id.videoView)
    PlayerView videoView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_video, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initializePlayer();
        playVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayer.stop();
        exoPlayer.release();
    }

    private void playVideo() {
        //Uri videoUri = Uri.parse(Prefrence.getVideoLink(getContext()));

        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent)).createMediaSource(Uri.parse(Helper.getTeacher().getVideo()));

        videoView.setPlayer(exoPlayer);

        //ep_video_view.player = exoPlayer

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

        //videoView.setPlayer(exoPlayer);
    }

    private void initializePlayer() {
        TrackSelector trackSelector =  new DefaultTrackSelector(getContext());
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory rendererFactory = new DefaultRenderersFactory(getContext());

        exoPlayer = new SimpleExoPlayer.Builder(getContext(), rendererFactory)
                .setLoadControl(loadControl)
                .setTrackSelector(trackSelector)
                .build();
    }
}
