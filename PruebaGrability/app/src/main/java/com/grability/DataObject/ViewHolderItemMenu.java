package com.grability.DataObject;

import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderItemMenu {

	private ImageView imagen;
	private TextView titulo;

	public ViewHolderItemMenu(ImageView imagen, TextView titulo) {

		this.imagen = imagen;
		this.titulo = titulo;
	}

	public ImageView getImagen() {
		return imagen;
	}

	public void setImagen(ImageView imagen) {
		this.imagen = imagen;
	}

	public TextView getTitulo() {
		return titulo;
	}

	public void setTitulo(TextView titulo) {
		this.titulo = titulo;
	}
	
	
}
