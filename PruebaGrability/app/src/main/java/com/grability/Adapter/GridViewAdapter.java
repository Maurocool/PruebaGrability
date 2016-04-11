package com.grability.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.grability.DataObject.Imagen;
import com.grability.PruebaGrability.R;
import com.grability.Tool.Const;
import com.grability.Tool.Util;

public class GridViewAdapter extends BaseAdapter {

	// un BaseAdapter puede usarse para un Adapter en un listview o gridview
	// hay que implementar algunos metodos heredados de la clase Adapter,
	// porque BaseAdapter es una subclase de Adapter
	// estos metodos en este ejemplo son: getCount(), getItem(), getItemId(),
	// getView()
	/**
	 * Contexto de la aplicacion
	 */
	private Context mContext;
	/**
	 * ArrayList con imagenes del GridViewAdapter
	 */
	private ArrayList<Imagen> listItems;
	private boolean[] animationStates;
	/**
	 * Tama o de la Imagen Dependiendo del dispositivo Utilizado
	 */
	private int size;

	// el constructor necesita el contexto de la actividad donde se utiliza
	// el
	// adapter
	public GridViewAdapter(Context c, ArrayList<Imagen> listItems, int heigth) {
		this.mContext = c;
		this.size = heigth;
		this.listItems = listItems;
		this.animationStates = new boolean[listItems.size()];
	}

	public int getCount() {// devuelve el n mero de elementos que se
							// introducen
		// en el adapter
		return listItems.size();
	}

	public Object getItem(int position) {
		// este m todo deber a devolver el objeto que esta en esa posici n
		// del
		// adapter. No es necesario en este caso m s que devolver un objeto
		// null.
		return null;
	}

	public long getItemId(int position) {
		// este m todo deber a devolver el id de fila del item que esta en
		// esa
		// posici n del adapter. No es necesario en este caso m s que
		// devolver 0.
		return 0;
	}

	// crear un nuevo ImageView para cada item referenciado por el Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * En este Metodo se utiliza el patron ViewHolder para optimizar 
		 * los items que se puedan general en el
		 * */
		View item = convertView;
		final ViewHolder viewHolder;
		
		LayoutInflater inflater =  ( LayoutInflater )mContext.
	            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (item == null) {
			
			item = new View(mContext);
			item = inflater.inflate(R.layout.gridview_item , null);
			item.setLayoutParams(new GridView.LayoutParams(size * Const.RESIZE_ITEM_CATALOG_WHIDTH , size * Const.RESIZE_ITEM_CATALOG_HEIGHT));
			
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) item.findViewById(R.id.imageViewItem);
			viewHolder.lblTitulo = (TextView) item.findViewById(R.id.textViewItem);
			item.setTag(viewHolder);
			
			if (!animationStates[position]) {
		          animationStates[position] = true;
		          Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
		          animation.setStartOffset(position * Const.ANIMATIO_START_OFF_SET);
		          item.startAnimation(animation);
		    }
			
		}else{
			
			viewHolder = (ViewHolder) item.getTag();
		}
		
		viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		viewHolder.imageView.setPadding(8, 8, 8, 8);
		Util.actualizarImagen(viewHolder.imageView, listItems.get(position).getUrlImagen(), mContext , Const.RESIZE_IMAGE_CATALOG);
		
		viewHolder.lblTitulo.setText(listItems.get(position).getImName());
		
	     return item;
	}
	
	
	// clase ViewHolder
	private static class ViewHolder {

		public ImageView imageView = null;
		public TextView lblTitulo = null;
		public TextView lblSubTitulo = null;
		public int position;
	}// end clase ViewHolder

}
