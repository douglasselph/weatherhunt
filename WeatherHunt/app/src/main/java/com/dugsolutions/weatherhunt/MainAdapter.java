package com.dugsolutions.weatherhunt;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dugsolutions.weatherhunt.data.ConditionDate;
import com.dugsolutions.weatherhunt.data.ConditionLocal;
import com.dugsolutions.weatherhunt.data.WeatherLocalResult;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by dug on 7/20/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon) ImageView iconView;
        @BindView(R.id.when) TextView whenView;
        @BindView(R.id.desc) TextView descView;
        @BindView(R.id.temperature) TextView temperatureView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static final char DEGREE = (char) 0x00B0;
    final Context mContext;
    WeatherLocalResult mResult;
    Integer mMaxHeight;

    public MainAdapter(Context context) {
        mContext = context;
    }

    public void setResult(WeatherLocalResult result) {
        mResult = result;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        ConditionLocal cond = mResult.getCondition(position);
        holder.whenView.setText(cond.time);
        holder.descView.setText(cond.weatherDesc);
        holder.temperatureView.setText(getTempString(cond.tempC, cond.tempF));
        Picasso.with(mContext).cancelRequest(holder.iconView);
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.listener(new Picasso.Listener() {
            @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Timber.e(exception);
                holder.iconView.setImageResource(android.R.color.transparent);
            }
        });
        try {
            builder.build()
                    .load(Uri.parse(cond.weatherIconUrl.toURI().toString()))
                    .placeholder(R.drawable.loading)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .centerInside()
                    .resize(0, getMaxHeight())
                    .into(holder.iconView);
        } catch (Exception ex) {
            holder.iconView.setImageResource(android.R.color.transparent);
        }
    }

    int getMaxHeight() {
        if (mMaxHeight == null) {
            mMaxHeight = (int) mContext.getResources().getDimension(R.dimen.icon_size);
        }
        return mMaxHeight;
    }

    String getTempString(Integer tempC, Integer tempF) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(tempC);
        sbuf.append("C");
        sbuf.append(DEGREE);
        sbuf.append("/");
        sbuf.append(tempF);
        sbuf.append("F");
        sbuf.append(DEGREE);
        return sbuf.toString();
    }

    @Override public int getItemCount() {
        if (mResult == null) {
            return 0;
        }
        return mResult.getCount();
    }
}
