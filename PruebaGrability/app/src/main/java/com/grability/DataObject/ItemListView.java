package com.grability.DataObject;

import java.io.Serializable;

public class ItemListView implements Serializable {

	private static final long serialVersionUID = 6L;
	
	/**
	 * Titulo del ItemListView
	 */
	private String titulo;
	/**
	 * Subtitulo del ItemListView
	 */
	private String subTitulo = "";
	/**
	 * Icono del ItemListView
	 */
	private int icono;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getSubTitulo() {
		return subTitulo;
	}
	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}
	public int getIcono() {
		return icono;
	}
	public void setIcono(int icono) {
		this.icono = icono;
	}
}