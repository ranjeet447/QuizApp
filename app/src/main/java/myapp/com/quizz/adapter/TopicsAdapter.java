package myapp.com.quizz.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import myapp.com.quizz.MainActivity;
import myapp.com.quizz.R;

/**
 * Created by ranje on 10/15/2017.
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicCustomViewHolder> {
    ArrayList<String> mArrayList;
    Context mContext;
    ArrayList<GradientDrawable> mGradientDrawables;

    public TopicsAdapter(Context context, ArrayList<String> arrayList) {
        mArrayList = arrayList;
        mContext = context;
        mGradientDrawables = new ArrayList<>();
        fillGradiantList(mContext);
    }

    public void addTopic(String topic) {
        mArrayList.add(topic);
    }

    @Override
    public TopicCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item_list_view, parent, false);
        return new TopicCustomViewHolder(view);
    }

    private void fillGradiantList(Context context) {
        mGradientDrawables.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_1_start), ContextCompat.getColor(context, R.color.gradient_1_end)));
        mGradientDrawables.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_2_start), ContextCompat.getColor(context, R.color.gradient_2_end)));
        mGradientDrawables.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_3_start), ContextCompat.getColor(context, R.color.gradient_3_end)));
        mGradientDrawables.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_4_start), ContextCompat.getColor(context, R.color.gradient_4_end)));

    }

    private GradientDrawable getTempGradientDrawable(int startColor, int endColor) {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[]{startColor, endColor});
        drawable.setDither(true);
        drawable.setGradientCenter(drawable.getIntrinsicWidth() / 8, drawable.getIntrinsicHeight() / 2);
        drawable.setCornerRadius(20);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setUseLevel(true);
        return drawable;
    }

    @Override
    public void onBindViewHolder(TopicCustomViewHolder holder, int position) {

        String topicName = mArrayList.get(position);
        holder.mTextView.setText(topicName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.mRelativeLayout.setBackground(mGradientDrawables.get(position % 4));
        } else {
            holder.mRelativeLayout.setBackgroundDrawable(mGradientDrawables.get(position % 4));
        }

        holder.mImage.setImageResource(R.drawable.ic_menu);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class TopicCustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTextView;
        private RelativeLayout mRelativeLayout;

        public TopicCustomViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.topicImageView);
            mTextView = (TextView) itemView.findViewById(R.id.topicTextView);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.topicRelativeLayout);
        }
    }
}
