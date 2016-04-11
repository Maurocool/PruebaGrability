package com.grability.DataObject;

import java.io.Serializable;

public class Imagen implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Identificador de la Imagen
	 */
	private String idImagen;
	/**
	 * Url de la Imagen
	 */
	private String urlImagen;
	/**
	 * Titulo de la Imagen
	 */
	private String tittle;
	/**
	 * Nombre de la Imagen
	 */
	private String imName;
	
	public String getIdImagen() {
		return idImagen;
	}
	public void setIdImagen(String idImagen) {
		this.idImagen = idImagen;
	}
	public String getUrlImagen() {
		return urlImagen;
	}
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	public String getTittle() {
		return tittle;
	}
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	public String getImName() {
		return imName;
	}
	public void setImName(String imName) {
		this.imName = imName;
	}
	
}
