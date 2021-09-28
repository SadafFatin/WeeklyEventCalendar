package co.jeeon.exam.eventcalender.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import co.jeeon.exam.eventcalender.R;
import co.jeeon.exam.eventcalender.interfaces.EventItemClickListener;
import co.jeeon.exam.eventcalender.models.Event;
import co.jeeon.exam.eventcalender.utils.UiUtils;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> eventsList;
    private Context context;
    private EventItemClickListener onEventItemClickListener;

    public EventAdapter(Context context, List<Event> eventsList, EventItemClickListener onEventItemClickListener) {

        this.context = context;
        this.eventsList = eventsList;
        this.onEventItemClickListener = onEventItemClickListener;

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
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_layout_event, parent, false);
        return new ViewHolder(listItem, onEventItemClickListener);
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
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {

        holder.eventTitle.setText(eventsList.get(position).getEventTitle());
        holder.eventDesc.setText(eventsList.get(position).getEventDesc());
        holder.eventDate.setText(UiUtils.formatDateIntoWeekDay(eventsList.get(position).getEventDate()));
        holder.viewBar.setBackgroundColor(context.getResources().getColor(getEventColor()));

        /*if (row_index == position ) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }*/

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void updateList() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView eventTitle,eventDesc,eventDate;
        Button eventDelete,eventEdit;
        View viewBar;
        private EventItemClickListener onEventListItemClickListener;

        public ViewHolder(@NonNull View itemView, EventItemClickListener onEventListItemClickListener) {
            super(itemView);
            this.eventTitle = itemView.findViewById(R.id.event_title);
            this.eventDesc = itemView.findViewById(R.id.event_desc);
            this.eventDate = itemView.findViewById(R.id.event_date);
            this.eventEdit = itemView.findViewById(R.id.event_edit);
            this.eventDelete = itemView.findViewById(R.id.event_delete);
            this.viewBar = itemView.findViewById(R.id.divider);

            this.onEventListItemClickListener = onEventListItemClickListener;
            this.eventDelete.setOnClickListener(this);
            this.eventEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.onEventListItemClickListener.onEventItemOnClick(view,getLayoutPosition(),eventsList.get(getLayoutPosition()));
        }
    }


    private int getEventColor() {
        return UiUtils.weekDayColor(getRandomWeekName());
    }
    private String getRandomWeekName() {
        return UiUtils.weekDays[new Random().nextInt(6 )];
    }

}
