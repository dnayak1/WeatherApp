package dhiraj.com.weatherapp;

/**
 * Created by dhira on 09-04-2017.
 */

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

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dhira on 09-03-2017.
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ListRecyclerViewHolder> {
    String outputDateString;
    Date inputDate,outputDate;
    ArrayList<CityData> arrayListGrid = new ArrayList<>();
    Context mContext;
    private IListListener listListener;

    public ListRecyclerAdapter(Context mContext, ArrayList<CityData> arrayListGrid, IListListener gridListener) {
        this.arrayListGrid = arrayListGrid;
        this.mContext = mContext;
        this.listListener = gridListener;

    }

    @Override
    public ListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_layout, parent, false);
        ListRecyclerAdapter.ListRecyclerViewHolder listRecyclerViewHolder = new ListRecyclerAdapter.ListRecyclerViewHolder(view);
        return listRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ListRecyclerViewHolder holder, int position) {
        final CityData cityData=arrayListGrid.get(position);
        holder.textViewListCountryCity.setText(cityData.getCityName()+", "+cityData.getCountry());
        holder.textViewListTemperature.setText("Temperature: "+cityData.getTemperature());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat simpleDateFormatNewFormat=new SimpleDateFormat("d MMM ''yy");
        String dateString=cityData.getUpdatedTime();
        try {
            inputDate=simpleDateFormat.parse(dateString);
            outputDateString=simpleDateFormatNewFormat.format(inputDate);
            outputDate=simpleDateFormatNewFormat.parse(outputDateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime=new PrettyTime();
        String stringPrettyTime=prettyTime.format(outputDate);
        holder.textViewListUpdated.setText("Last Updated: "+stringPrettyTime);
        /*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat simpleDateFormatNewFormat=new SimpleDateFormat("d MMM ''yy");
        String dateString=cityData.getUpdatedTime();
        try {
            inputDate=simpleDateFormat.parse(dateString);
            outputDateString=simpleDateFormatNewFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewListUpdated.setText(outputDateString);*/
        if(cityData.getFavorite()){
            holder.imageButtonListStarGrey.setVisibility(View.INVISIBLE);
            holder.imageViewListStarGold.setVisibility(View.VISIBLE);
        }
        else{
            holder.imageButtonListStarGrey.setVisibility(View.VISIBLE);
            holder.imageViewListStarGold.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listListener.deleteWeather(cityData);
                return true;
            }
        });
        holder.imageButtonListStarGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.makeFavorite(cityData);
            }
        });
        holder.imageViewListStarGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.removeFavorite(cityData);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListGrid.size();
    }

    public static class ListRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewListStarGold;
        ImageView imageButtonListStarGrey;
        TextView textViewListCountryCity;
        TextView textViewListUpdated;
        TextView textViewListTemperature;

        public ListRecyclerViewHolder(View itemView) {
            super(itemView);
            imageViewListStarGold = (ImageView) itemView.findViewById(R.id.imageViewStarGold);
            imageButtonListStarGrey = (ImageView) itemView.findViewById(R.id.imageViewStarGrey);
            textViewListCountryCity = (TextView) itemView.findViewById(R.id.textViewListRecyclerCountryCity);
            textViewListUpdated = (TextView) itemView.findViewById(R.id.textViewListRecyclerUpdated);
            textViewListTemperature = (TextView) itemView.findViewById(R.id.textViewTemperatureListRecycler);
        }
    }

    interface IListListener {
        void deleteWeather(CityData cityData);
        void makeFavorite(CityData cityData);
        void removeFavorite(CityData cityData);
    }
}

