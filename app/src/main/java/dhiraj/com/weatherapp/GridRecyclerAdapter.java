package dhiraj.com.weatherapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dhira on 09-03-2017.
 */

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridRecyclerViewHolder> {
    ArrayList<CityWeatherDetails> arrayListGrid=new ArrayList<>();
    Context mContext;
    private IGridListener gridListener;
    String dayImageUrl, outputDateString;
    Date inputDate;

    public GridRecyclerAdapter(Context mContext, ArrayList<CityWeatherDetails> arrayListGrid, IGridListener gridListener) {
        this.arrayListGrid = arrayListGrid;
        this.mContext = mContext;
        this.gridListener = gridListener;

    }

    @Override
    public GridRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view= layoutInflater.inflate(R.layout.grid_item_layout,parent,false);
        GridRecyclerAdapter.GridRecyclerViewHolder gridRecyclerViewHolder=new GridRecyclerAdapter.GridRecyclerViewHolder(view);
        return gridRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(GridRecyclerViewHolder holder, int position) {
        final CityWeatherDetails cityWeatherDetails=arrayListGrid.get(position);
        if(Integer.parseInt(cityWeatherDetails.getDayImage())<10){
            dayImageUrl="0"+cityWeatherDetails.getDayImage();
        }
        else{
            dayImageUrl=cityWeatherDetails.getDayImage();
        }
        String dayImageIconUrl="http://developer.accuweather.com/sites/default/files/"+dayImageUrl+"-s.png";
        Picasso.with(mContext).load(dayImageIconUrl).into(holder.imageViewGridImage);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat simpleDateFormatNewFormat=new SimpleDateFormat("d MMM ''yy");
        String dateString=cityWeatherDetails.getForecastDate();
        try {
            inputDate=simpleDateFormat.parse(dateString);
            outputDateString=simpleDateFormatNewFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewDate.setText(outputDateString);
        holder.imageViewGridImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridListener.showWeatherDetail(cityWeatherDetails);
            }
        });
        holder.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridListener.showWeatherDetail(cityWeatherDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListGrid.size();
    }

    public static class GridRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewGridImage;
        TextView textViewDate;
        public GridRecyclerViewHolder(View itemView) {
            super(itemView);
            imageViewGridImage= (ImageView) itemView.findViewById(R.id.imageViewRecycler);
            textViewDate= (TextView) itemView.findViewById(R.id.textViewRecycler);
        }
    }

    interface IGridListener
    {
        void showWeatherDetail(CityWeatherDetails cityWeatherDetails);
    }
}