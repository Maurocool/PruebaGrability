package com.grability.BusinessObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.grability.DataObject.Category;
import com.grability.DataObject.Entry;
import com.grability.DataObject.Imagen;
import com.grability.DataObject.ItemListView;
import com.grability.Tool.Util;

public class DataBaseBO {
	
	public static final String TAG = "BusinessObject.DataBaseBO";
	private static String msj;
	
	public static boolean crearDatabase() {
		
		SQLiteDatabase db = null;
		
		try {
    		
			File dbFile = new File(Util.getDirApp(), "DataBase.db");
    		db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    		
     		String query = "CREATE TABLE IF NOT EXISTS [Entry] ([id] varchar(15), [im_name] varchar(100), [summary] varchar, [im_price] varchar(10), [currency] varchar(4), "
     				+ "[im_contentType] varchar(10), [rights] varchar(50), [title] varchar(100), [link] varchar, [im_artist] varchar(100), [category] varchar(50), "
     				+ "[id_category] varchar(5), [im_releaseDate] varchar(50))";
        	db.execSQL(query);
        	
     		query = "CREATE TABLE IF NOT EXISTS [Image] ([id_entry] VARCHAR(15) NOT NULL, [url] VARCHAR, [urlInterna] VARCHAR, [heigth] INT(5), [Imagen] blob)";
        	db.execSQL(query);
        	
        	query = "DELETE FROM [Entry] ";
        	db.execSQL(query);
        	query = "DELETE FROM [Image] ";
        	db.execSQL(query);

    		return true;
			
        } catch (Exception e) {
        	
        	Log.e(TAG, "crearDatabase -> " + e.getMessage(), e);
        	return false;
        	
		} finally {
			
			closeDataBase(db);
		}
	}
	
	/**
	 * Metodo que inserta, en la tabla (Entry) los recursos obtenidos en el archivos Json
	 * 
	 * @param cvEntry ContentValues con los valores a insertar en la tabla (Entry) 
	 * @return boolean, retorna true si la insercion fue exitosa, de lo contrario retorna false
	 */
	public static boolean insertTableEntry(ContentValues cvEntry) {

		SQLiteDatabase db = null;

		try {

			File dbFile = new File(Util.getDirApp(), "DataBase.db");
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);

			db.insertOrThrow("Entry", null, cvEntry);

			return true;

		} catch (Exception e) {

			msj = e.getMessage();
			Log.e(TAG, "insertTableEntry: " + msj, e);
			return false;

		} finally {

			if (db != null)
				db.close();

		}
	}

	/**
	 * Metodo que inserta, en la tabla (Image) los recursos obtenidos en el archivos Json
	 * 
	 * @param cvImage ContentValues con los valores a insertar en la tabla (Image) 
	 * @return boolean, retorna true si la insercion fue exitosa, de lo contrario retorna false
	 */
	public static boolean insertTableImage(ContentValues cvImage) {

		SQLiteDatabase db = null;

		try {

			File dbFile = new File(Util.getDirApp(), "DataBase.db");
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);

			db.insertOrThrow("Image", null, cvImage);

			return true;

		} catch (Exception e) {

			msj = e.getMessage();
			Log.e(TAG, "insertTableImage: " + msj, e);
			return false;

		} finally {

			closeDataBase(db);

		}
	}

	/**
	 * metodo que retorna si esiste la base de datos
	 * @return
	 */
	public static boolean existeDataBase() {
		
		File dbFile = new File(Util.getDirApp(), "DataBase.db");
		return dbFile.exists();
	}
	
	
	
	/**
	 * Metodo Encargado de leer las categorias de la base de datos
	 * @return ArrayList<Category>, retorna un ArrayList de Categorias
	 */
	public static ArrayList<Category> getListaCategorias(List<ItemListView> listItems) {

		SQLiteDatabase db = null;
		Category category;
		ItemListView itemListView;
		ArrayList<Category> listaCategorias = new ArrayList<Category>();

		try {

			File dbFile = new File(Util.getDirApp(), "DataBase.db");
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null,
					SQLiteDatabase.OPEN_READWRITE);
			

			String query = "SELECT distinct id_category as id_category, category as category "
					+ "FROM Entry ORDER BY category";
			
			Cursor cursor = db.rawQuery(query, null);

			while (cursor.moveToNext()) {

				category = new Category();
				category.setIdCategoria(cursor.getString(cursor.getColumnIndex("id_category")));
				category.setCategoria(cursor.getString(cursor.getColumnIndex("category")));
				listaCategorias.add(category);

				itemListView = new ItemListView();
				itemListView.setTitulo(category.getCategoria());
				listItems.add(itemListView);
			}

			if (cursor != null)
				cursor.close();

		} catch (Exception e) {

			Log.e("getListaCategorias", e.getMessage(), e);

		} finally {

			closeDataBase(db);
		}

		return listaCategorias;
	}
	
	/**
	 * 
	 * Metodo Encargado de leer las imagense de la base de datos, dependiendo del tamannio
	 * @param Categoria, categoria seleccionada
	 * @param heigth
	 * @return ArrayList<Imagen>, retorna un ArrayList de Imagenes
	 */
	public static ArrayList<Imagen> getListaImagenes(String Categoria, int heigth) {

		SQLiteDatabase db = null;
		Imagen imagen;
		ArrayList<Imagen> listaImagen = new ArrayList<Imagen>();

		try {

			File dbFile = new File(Util.getDirApp(), "DataBase.db");
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null,
					SQLiteDatabase.OPEN_READWRITE);
			

			String query = "SELECT distinct I.id_entry as id, I.urlInterna as url, E.title as tittle, E.im_name as name "
					+ "FROM Image I "
					+ "JOIN Entry E ON I.id_entry = E.id  WHERE heigth = '" + heigth + "' "
					//Si la categoria esta vacia se muestran todas las imagenes, de lo contrario se buscan por categoria
					+ (Categoria.isEmpty() ? "" : " AND E.id_category = '" + Categoria + "' ") ;
			
			Cursor cursor = db.rawQuery(query, null);

			if (cursor.moveToFirst()) {

				do {

					imagen = new Imagen();
					imagen.setIdImagen(cursor.getString(cursor.getColumnIndex("id")));
					imagen.setUrlImagen(cursor.getString(cursor.getColumnIndex("url")));
					imagen.setImName(cursor.getString(cursor.getColumnIndex("name")));

					listaImagen.add(imagen);

				} while (cursor.moveToNext());
			}

			if (cursor != null)
				cursor.close();

		} catch (Exception e) {

			Log.e("getListaImagenes", e.getMessage(), e);

		} finally {

			closeDataBase(db);
		}

		return listaImagen;
	}

	/**
	 * Metodo Encargado de actualizar las Urls Internas de las Imagenes descargadas
	 * @param ruta
	 * @param height
	 * @param id
	 * @return boolean, retorna true si la insercion fue exitosa, de lo contrario retorna false
	 */
	public static boolean actualizarImagen(String ruta, String height, String id) {
		
		SQLiteDatabase db = null;

		boolean state = true;

		try {

			File dbFile = new File(Util.getDirApp(), "DataBase.db");
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);

			
			/*******************************
			 * Se actualizan los datos de la Imagen
			 *******************************/
			ContentValues values = new ContentValues();
			values.put("urlInterna", ruta);
			
			String whereClause = "id_entry = ? AND heigth = ?";
			String whereArgs[] = new String[] { id, height };
				
			db.update("Image", values, whereClause , whereArgs);
			
		} catch (Exception e) {
			Log.e("actualizarImagen", e.getMessage(), e);
			state = false;
		} finally {

			closeDataBase(db);

		}

		return state;		
	}
	
	
	/**
	 * Metodo encargado de cerrar la conexion a un Bd en especifico
	 * 
	 * @param db
	 */
	public static void closeDataBase(SQLiteDatabase db) {

		if (db != null) {

			if (db.inTransaction()) {
				db.endTransaction();
			}

			db.close();
		}
	}
	
	
	/**
	 * Metodo que obtiene un objeto tipo Entry de la base de datos
	 * @param idEntry
	 * @param heigth
	 * @return
	 */
	public static Entry getEntry(String idEntry, String heigth){

		SQLiteDatabase db = null;
		Entry entry = null;

		try {

			File dbFile = new File(Util.getDirApp(), "DataBase.db");
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null,
					SQLiteDatabase.OPEN_READWRITE);
			
			String query = "SELECT distinct I.id_entry as id, I.urlInterna as url, E.title as tittle, E.im_name as name, E.summary as summary, "
					+ "im_price as im_price, currency as currency, im_contentType as im_contentType, rights as rights, link as link, im_artist as im_artist, "
					+ "category as category, id_category as id_category, im_releaseDate as im_releaseDate "
					+ "FROM Image I "
					+ "JOIN Entry E ON I.id_entry = E.id  WHERE I.id_entry  = '" + idEntry + "' AND I.heigth = '" + heigth + "'";
			
			Cursor cursor = db.rawQuery(query, null);

			if (cursor.moveToFirst()) {

					entry = new Entry();
					entry.setImName(cursor.getString(cursor.getColumnIndex("name")));
					entry.setSummary(cursor.getString(cursor.getColumnIndex("summary")));
					entry.setImPrice(cursor.getString(cursor.getColumnIndex("im_price")));
					entry.setCurrency(cursor.getString(cursor.getColumnIndex("currency")));
					entry.setImContentType(cursor.getString(cursor.getColumnIndex("im_contentType")));
					entry.setRights(cursor.getString(cursor.getColumnIndex("rights")));
					entry.setTitle(cursor.getString(cursor.getColumnIndex("tittle")));
					entry.setLink(cursor.getString(cursor.getColumnIndex("link")));
					entry.setId(cursor.getString(cursor.getColumnIndex("id")));
					entry.setImArtist(cursor.getString(cursor.getColumnIndex("im_artist")));
					entry.setCategory(cursor.getString(cursor.getColumnIndex("category")));
					entry.setIdCategory(cursor.getString(cursor.getColumnIndex("id_category")));
					entry.setImReleaseDate(cursor.getString(cursor.getColumnIndex("im_releaseDate")));
					entry.setUrlImagen(cursor.getString(cursor.getColumnIndex("url")));
			}

			if (cursor != null)
				cursor.close();

		} catch (Exception e) {

			Log.e("listaImagenes", e.getMessage(), e);

		} finally {

			closeDataBase(db);
		}

		return entry;
	}
	

}
