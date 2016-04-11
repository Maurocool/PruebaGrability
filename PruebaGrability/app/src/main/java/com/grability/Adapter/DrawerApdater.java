package com.grability.Adapter;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grability.DataObject.DrawerItem;
import com.grability.DataObject.ViewHolderItemMenu;
import com.grability.PruebaGrability.R;

public class DrawerApdater extends BaseAdapter {

	private Context context;
	private Vector<DrawerItem> listItems;

	public DrawerApdater(Context context, Vector<DrawerItem> listItems) {

		this.context = context;
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderItemMenu viewHolder;
		DrawerItem item = listItems.elementAt(position);
		if (convertView == null) {

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			convertView = mInflater.inflate(R.layout.drawer_list_item, parent,
					false);

			viewHolder = new ViewHolderItemMenu(
					(ImageView) convertView.findViewById(R.id.icon),
					(TextView) convertView.findViewById(R.id.title));

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolderItemMenu) convertView.getTag();
		}

		if (item != null) {

			viewHolder.getTitulo().setText(item.getTitulo());
			viewHolder.getImagen().setImageResource(item.getIcono());

		}

		return convertView;
	}

}
