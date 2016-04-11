package com.grability.DataObject;

public class DrawerItem {

	/**
	 * Titlo del DrawerItem
	 */
	private String titulo;
	/**
	 * Icono del del DrawerItem
	 */
	private int icono;

	public DrawerItem(String titulo, int icono) {
		super();
		this.titulo = titulo;
		this.icono = icono;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

}
