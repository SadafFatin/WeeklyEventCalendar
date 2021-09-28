package co.jeeon.exam.eventcalender.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.jeeon.exam.eventcalender.R;
import co.jeeon.exam.eventcalender.interfaces.WeekDaysItemClickListener;
import co.jeeon.exam.eventcalender.models.WeekDay;
import co.jeeon.exam.eventcalender.utils.UiUtils;

public class WeekDaysAdapter extends RecyclerView.Adapter<WeekDaysAdapter.ViewHolder> {

    private List<WeekDay> weekDayList;
    private Context context;
    private WeekDaysItemClickListener onWeekDayItemClickListener;
    private int row_index;

    public WeekDaysAdapter(Context context, List<WeekDay> weekDayList, WeekDaysItemClickListener onWeekDayItemClickListener) {

        this.context = context;
        this.weekDayList = weekDayList;
        this.onWeekDayItemClickListener = onWeekDayItemClickListener;

    }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     */
    @NonNull
    @Override
    public WeekDaysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_layout_weekday, parent, false);
        return new ViewHolder(listItem, onWeekDayItemClickListener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override onBindViewHolder instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull WeekDaysAdapter.ViewHolder holder, int position) {

        holder.weekDay.setText(weekDayList.get(position).getWeekDay()+"\n"+weekDayList.get(position).getWeekDate());
        holder.weekDay.setTextColor(context.getResources().getColor(getWeekDayColor(weekDayList.get(position).getWeekDay())));

        if (row_index == position ) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return weekDayList.size();
    }

    public void setCurrentSelectedPosition(int position) {
        this.row_index = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView weekDay;
        private WeekDaysItemClickListener onWeekDayListItemClickListener;

        public ViewHolder(@NonNull View itemView, WeekDaysItemClickListener onWeekDayListItemClickListener) {
            super(itemView);
            this.weekDay = itemView.findViewById(R.id.weekday);
            this.onWeekDayListItemClickListener = onWeekDayListItemClickListener;
            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {
            this.onWeekDayListItemClickListener.onWeekDayItemOnClick(getLayoutPosition());
            row_index = getLayoutPosition();
            notifyDataSetChanged();
        }
    }


    private int getWeekDayColor(String weekday_name) {
        return UiUtils.weekDayColor(weekday_name);
    }


}
