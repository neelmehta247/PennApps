package com.example.rahul.pennapps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahul.pennapps.Helpers.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by neel on 22/01/17 at 4:32 AM.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewholder> {
    Context context;
    ArrayList<Event> events;

    EventListAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new EventViewholder(inflater.inflate(R.layout.event_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewholder holder, int position) {
        final Event event = events.get(position);

        Picasso.with(context).load(event.imageUrl).into(holder.imageView);
        holder.titleText.setText(event.title);
        holder.descriptionText.setText(event.description);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventInfoActivity.class);
                intent.putExtra("event", event);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText, descriptionText;
        CardView cardView;

        public EventViewholder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            descriptionText = (TextView) itemView.findViewById(R.id.description_text);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}
