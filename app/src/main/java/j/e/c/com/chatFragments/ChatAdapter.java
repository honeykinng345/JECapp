package j.e.c.com.chatFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.chatFragments.models.Message;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    static final int MSG_TYPE_LEFT = 0;
    static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Message> messageList;

    public ChatAdapter(Context mContext, List<Message> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT)
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sender, viewGroup, false);
        else view = LayoutInflater.from(mContext).inflate(R.layout.item_receive, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.showMessage.setText(messageList.get(i).getMessage());
        viewHolder.time.setText(messageList.get(i).getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFrm().toLowerCase().equals("u"))
            return MSG_TYPE_RIGHT;
        else return MSG_TYPE_LEFT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.show_message)
        TextView showMessage;
        @BindView(R.id.time)
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
