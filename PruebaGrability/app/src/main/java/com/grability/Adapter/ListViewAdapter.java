package com.grability.Adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grability.DataObject.ItemListView;
import com.grability.PruebaGrability.R;
import com.grability.Tool.Const;

public class ListViewAdapter extends ArrayAdapter<ItemListView> {
	
	/**
	 * Icono del ListViewAdapter
	 */
	private int icono;
	/**
	 * Color del titulo del ListViewAdapter
	 */
	private int colorTitulo;
	/**
	 * Contexto de la aplicacion 
	 */
	private Activity context;
	/**
	 * Lista con los elementos entrantes al ListViewAdapter
	 */
	private List<ItemListView> listItems;
	/**
	 * Arreglo de booleans encargado de guardar los estados de las animaciones
	 */
	private boolean[] animationStates;
	
	public ListViewAdapter(Activity context, List<ItemListView> listItems, int icono, int colorTitulo) {
		
		super(context, R.layout.list_item, listItems);
		this.listItems = listItems; 
		this.context = context;
		this.icono = icono;
		this.colorTitulo = colorTitulo; 
		this.animationStates = new boolean[listItems.size()];
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View item = null;
		
		final ViewHolder viewHolder;
		
		if (convertView == null){
			
			LayoutInflater inflater = context.getLayoutInflater();
			item = inflater.inflate(R.layout.list_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.lblTitulo = (TextView)item.findViewById(R.id.lblTitulo);
			viewHolder.lblSubtitulo = (TextView)item.findViewById(R.id.lblSubTitulo);
			item.setTag(viewHolder);
			
		      if (!animationStates[position]) {
		            animationStates[position] = true;
		            
		            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left_to_right);
		            animation.setStartOffset(position * Const.ANIMATIO_START_OFF_SET);
		            item.startAnimation(animation);
		        }
			
		}else{
			
			item = convertView;    
			viewHolder = (ViewHolder)item.getTag();
		} 
		
			viewHolder.lblTitulo.setText(listItems.get(position).getTitulo());
			viewHolder.lblTitulo.setTextColor(colorTitulo);
			viewHolder.lblSubtitulo.setText(listItems.get(position).getSubTitulo());
		
		if( listItems.get(position).getSubTitulo().equals( "" ))
			viewHolder.lblSubtitulo.setVisibility( View.GONE );
		else
			viewHolder.lblSubtitulo.setVisibility( View.VISIBLE );
		
		if (listItems.get(position).getIcono() > Const.SIN_ICONO)
			((ImageView)item.findViewById(R.id.iconListView)).setImageResource(listItems.get(position).getIcono());
		else if (icono > Const.SIN_ICONO)
			((ImageView)item.findViewById(R.id.iconListView)).setImageResource(icono);

		return item;
    }
	
	@Override
	public ItemListView getItem(int position) {
		
		return listItems.get(position);
	}
	
	
	/**
	 * Clase ViewHolder para usarse como patron de optimizacion
	 * @author ASUS
	 *
	 */
	public static class ViewHolder {
		
		/**
		 * Titulo del ViewHolder
		 */
		protected TextView lblTitulo;
		/**
		 * Subtitulo del ViewHolder
		 */
		protected TextView lblSubtitulo;
	}  
}
