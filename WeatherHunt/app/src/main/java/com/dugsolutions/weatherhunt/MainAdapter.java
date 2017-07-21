package com.dugsolutions.weatherhunt;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dugsolutions.weatherhunt.data.BitmapHelper;
import com.dugsolutions.weatherhunt.data.ConditionLocal;
import com.dugsolutions.weatherhunt.data.WeatherLocalResult;
import com.squareup.picasso.Callback;
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
        @BindView(R.id.line) TextView lineView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    final Context mContext;
    WeatherLocalResult mResult;
    int mTargetIconSize;

    public MainAdapter(Context context) {
        mContext = context;
        mTargetIconSize = (int) context.getResources().getDimension(R.dimen.icon_size);
        ConditionLocal.InitRaindrop(mContext);
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
        final ConditionLocal cond = mResult.getCondition(position);
        holder.lineView.setText(cond.getLine());
        if (BitmapHelper.getInstance().hasBitmap(cond.weatherCode)) {
            holder.iconView.setImageBitmap(BitmapHelper.getInstance().getBitmap(cond.weatherCode));
        } else {
            Uri uri = cond.getIconUri();
            if (uri != null) {
                Picasso.with(mContext).cancelRequest(holder.iconView);
                Picasso.Builder builder = new Picasso.Builder(mContext);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception);
                    }
                });
                builder.build()
                        .load(uri)
                        .placeholder(R.drawable.loading)
                        .resize(mTargetIconSize, mTargetIconSize)
                        .into(holder.iconView, new Callback() {
                            @Override
                            public void onSuccess() {
                                BitmapHelper.getInstance().storeBitmap(cond.weatherCode, holder.iconView);
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mResult == null) {
            return 0;
        }
        return mResult.getCount();
    }
}
