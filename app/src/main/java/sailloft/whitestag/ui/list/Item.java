package sailloft.whitestag.ui.list;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by David's Laptop on 5/23/2015.
 */
public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
