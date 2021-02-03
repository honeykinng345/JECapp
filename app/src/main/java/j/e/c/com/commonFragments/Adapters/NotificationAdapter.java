package j.e.c.com.commonFragments.Adapters;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.Models.Notifications;
import j.e.c.com.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ArrayList<Notifications> notificationArrayList;
    Fragment context;

    public NotificationAdapter(ArrayList<Notifications> notificationArrayList, Fragment context) {
        this.notificationArrayList = notificationArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Notifications notifications = notificationArrayList.get(i);
        viewHolder.notifMessage.setText(""+notifications.getLink());
        viewHolder.notifMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchZoomUrl(notifications.getLink());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notif_message)
        TextView notifMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void launchZoomUrl(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        if (intent.resolveActivity(context.getActivity().getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
