package sailloft.whitestag.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sailloft.whitestag.R;

/**
 * Created by David's Laptop on 5/23/2015.
 */
public class Header implements Item {
    private final String         name;

    public Header(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return ListHeadersAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.view_header, null);
            // Do some initialization

        } else {
            view = convertView;
        }

        TextView text;
        text = (TextView) view.findViewById(R.id.headerText);
        text.setText(name);

        return view;
    }

}

