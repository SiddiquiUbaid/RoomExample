package Adapter;



import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.appsomniacs.roomexample.Dictionary;
import com.appsomniacs.roomexample.R;

import java.util.ArrayList;
import java.util.List;

public class MeaningsListAdapter extends RecyclerView.Adapter<MeaningsListAdapter.ViewHolder> {
    Context context;
    List<Dictionary> meaningsList;





    private OnMeaningsListener mOnMeaningsListener;

    public MeaningsListAdapter(Context applicationContext, List<Dictionary> meaningsList,
                             OnMeaningsListener OnMeaningsListener) {
        this.context=applicationContext;
        this.meaningsList=meaningsList;
        this.mOnMeaningsListener = OnMeaningsListener;

    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull   ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_meaning_in_list,parent,false);
        return new ViewHolder(view, mOnMeaningsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {



        holder.meaning.setText(meaningsList.get(position).getWord());

        holder.meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnMeaningsListener.onMeaningClick(position);

            }
        });









    }


    @Override
    public int getItemCount() {
        return meaningsList.size();
    }


    public void clear(){
        int size = getItemCount();
        meaningsList.clear();
        notifyItemRangeRemoved(0, size);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView meaning;
        OnMeaningsListener onMeaningsListener;

        public ViewHolder(@NonNull View itemView, OnMeaningsListener mOnMeaningsListener) {
            super(itemView);
            meaning=itemView.findViewById(R.id.tvShowMeaning);

        }

        @Override
        public void onClick(View view) {

            // invoking item click listener
//            onHotelListener.onWishListClick(getAdapterPosition());
//            onHotelListener.onHotelClick(getAdapterPosition());


        }


    }

    public interface OnMeaningsListener{
        void onMeaningClick(int position);

    }
}
