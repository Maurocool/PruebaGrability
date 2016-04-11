package com.grability.Conection;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.grability.BusinessObject.DataBaseBO;
import com.grability.PruebaGrability.R;
import com.grability.Tool.Const;
import com.grability.Tool.Util;

public class UtilSync extends Thread {

	/**
	 * Clase que se encarga de procesar la Respuesta
	 * del Servidor cuando finaliza el proceso de
	 * Sincronizacion.
	 */
	private RespSync respSync;
	
	/**
	 * Indica el tipo de codeRequest a Sincronizar 
	 */
	private int codeRequest;

	/**
	 * Contexto de la aplicacion
	 */
	private Context context;

	private boolean correcto;

	public UtilSync(RespSync respSync, int codeRequest, Context context) {
		// TODO Auto-generated constructor stub

		this.respSync = respSync;
		this.codeRequest = codeRequest;
		this.context = context;
	}

	public void run() {

		switch (codeRequest) {
		
		case Const.SYNC_PRUEBA:
			cargarDatos(Const.JSON);
//			cargarDatos(Const.JSON);
			break;

		default:
			break;
		}
	}
	
	private synchronized void cargarDatos(String url) {
		
		//It indicates the status of the transaction
		correcto = false;
		
		//Mensaje a retornar
		String mensaje = context.getString(R.string.msj_error_carga_info);
		
		//the connection is verified to synchronize the file Json
		if (!connectionTest()){
			
			mensaje = context.getString(R.string.msj_error_conexion);
			
			respSync.respSync(correcto, mensaje, codeRequest);
			return;
		}
		
		ContentValues cvImage;

		try {
			
			//Create the DataBase
			DataBaseBO.crearDatabase();
			
			// Making a request to server getting entities
			JSONObject json = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(url)).getEntity()));
			// getting json root object
			
			JSONObject feedObject = json.getJSONObject("feed");
			// getting needed array in root json object
			JSONArray entryArray = feedObject.getJSONArray("entry");

			// moving though the array
			for (int i = 0; i < entryArray.length(); i++) {
				// getting all objects in array
				JSONObject entryObjects = entryArray.getJSONObject(i);
				// taking objects with needed key
				
				// getting string name
				String name = entryObjects.getJSONObject("im:name").getString("label");
				// getting string summary
				String summary = entryObjects.getJSONObject("summary").getString("label");
				
				JSONObject entryObjectsPrice = entryObjects.getJSONObject("im:price").getJSONObject("attributes");
				// getting string amount imPrice
				String imPrice = entryObjectsPrice.getString("amount");
				// getting string currency
				String currency = entryObjectsPrice.getString("currency");
				
				// getting string im:contentType
				String im_contentType = entryObjects.getJSONObject("im:contentType").getJSONObject("attributes").getString("label");
				
				// getting string rights
				String rights = entryObjects.getJSONObject("rights").getString("label");
				
				// getting string title
				String title = entryObjects.getJSONObject("title").getString("label");
				// getting string link
				String link = entryObjects.getJSONObject("link").getJSONObject("attributes").getString("href");
				// getting string id
				String id = entryObjects.getJSONObject("id").getJSONObject("attributes").getString("im:id");
				// getting string im:artist
				String im_artist = entryObjects.getJSONObject("im:artist").getString("label");
				
				JSONObject entryObjectsCategory = entryObjects.getJSONObject("category").getJSONObject("attributes");
				// getting string category 
				String category = entryObjectsCategory.getString("term");
				// getting string amount id_category
				String id_category = entryObjectsCategory.getString("im:id");
				
				// getting string im:releaseDate
				String im_releaseDate = entryObjects.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label");

				cvImage = new ContentValues();
				
				cvImage.put(Const.DB_ENTRY_ID_ENTRY, id);
				cvImage.put(Const.DB_ENTRY_IM_NAME, name);
				cvImage.put(Const.DB_ENTRY_SUMMARY, summary);
				cvImage.put(Const.DB_ENTRY_IM_PRICE, imPrice);
				cvImage.put(Const.DB_ENTRY_CURRENCY, currency);
				cvImage.put(Const.DB_ENTRY_IM_CONTENTTYPE, im_contentType);
				cvImage.put(Const.DB_ENTRY_RIGHTS, rights);
				cvImage.put(Const.DB_ENTRY_TITTLE, title);
				cvImage.put(Const.DB_ENTRY_LINK, link);
				cvImage.put(Const.DB_ENTRY_IM_ARTIST, im_artist);
				cvImage.put(Const.DB_ENTRY_CATEGORY, category);
				cvImage.put(Const.DB_ENTRY_ID_CATEGORY, id_category);
				cvImage.put(Const.DB_ENTRY_IM_RELEASEDATE, im_releaseDate);
				
				//insert into DataBase Entry
				DataBaseBO.insertTableEntry(cvImage);
				
				// getting needed array in root json object
				JSONArray entryArrayImage = entryObjects.getJSONArray("im:image");
				
				// moving though the array
				for (int j = 0; j < entryArrayImage.length(); j++) {
					JSONObject entryObjectsImage = entryArrayImage.getJSONObject(j);
					String urlImage = entryObjectsImage.getString("label");
					String height = entryObjectsImage.getJSONObject("attributes").getString("height");
					
					cvImage = new ContentValues();
					
					cvImage.put(Const.DB_IMAGE_ID_ENTRY, id);
					cvImage.put(Const.DB_IMAGE_URL, urlImage);
					cvImage.put(Const.DB_IMAGE_HEIGTH, height);
//					cvImage.put(Const.DB_IMAGE_IMAGEN, name);
					
					//insert into DataBase Image
					DataBaseBO.insertTableImage(cvImage);
					descargarImagen(urlImage, id, height);
				}
				
				Log.d("", "" + name);
				
				//se cargo correctamente la informacion
				correcto = true;
				mensaje = context.getString(R.string.msj_carga_info_exitoso);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		respSync.respSync(correcto, mensaje, codeRequest);
	}
	
	/**
	 * Method testing the connection status
	 * @return
	 */
	private boolean connectionTest() {
		
		try {
		    URL url = new URL("http://www.google.com");

		    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		    urlc.setRequestProperty("User-Agent", "Android Application:"+ "prueba");
		    urlc.setRequestProperty("Connection", "close");
		    urlc.setConnectTimeout(1000 * 3); // mTimeout is in seconds
		    urlc.connect();
		    
		    if (urlc.getResponseCode() == Const.ESTADO_CONEXION_CORRECTO) {
		        return true;
		    }
		} catch (MalformedURLException e1) {
		    e1.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return false;
	}

	
	/**
	 * Metodo que Organiza, descarga y guarda la imagenes del catalogo
	 * 
	 * @param urlImage, direccion de la imagen 
	 * @param nombre, nombre de la imagen
	 * @param height, tamanio de la imagen
	 * @return boolean, retorna true si se pudo descargar y guardar,
	 *  de lo contrario retorna false
	 */
	public boolean descargarImagen(String urlImage, String nombreImagen, String height) {
		
		//Defino la ruta donde busco los ficheros
		File fileCarpetaGral = new File(Util.getDirApp().getPath() + Const.DIR_CATALOGO );
		
		//si el directorio no existe se crea
		if(!fileCarpetaGral.exists())
			crearcarpetaCatalogo();
		
		String ruta = Util.getDirApp().getPath() +  Util.obtenerDirCarpetaImgTamanio(height) + "/" + nombreImagen + ".png"; 
		
		// Se descarga la imagen de la Url
		Bitmap imagen = descargarImagen(urlImage);
		
		//verifica que la imagen no sea nula y la guarda
		if(imagen != null){
			if(guardarImagen(ruta, height, imagen))
				DataBaseBO.actualizarImagen(ruta, height, nombreImagen);
				
			return true;
		}else 
			return false;
		
	}
	
	//Metodo encargado de guardar las imagenes descargadas
	/**
	 * Metodo que guarda la imagen descargada en la carpeta correspondiente
	 * @param rutaImagen
	 * @param height
	 * @param imagen
	 * @return
	 */
	private boolean guardarImagen(String rutaImagen, String height, Bitmap imagen) {
		// TODO Auto-generated method stub
		
		FileOutputStream out = null;
		try {
		    out = new FileOutputStream(rutaImagen);
		    
		    imagen.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
		    // PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
		    e.printStackTrace();
		    return false;
		} finally {
		    try {
		        if (out != null) {
		            out.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        return false;
		    }
		}
		
		return true;
	}

	/**
	 * Metodo encargado de descargar una imagen de la Url
	 * @param imageHttpAddress
	 * @return
	 */
	private Bitmap descargarImagen (String imageHttpAddress){
	    URL imageUrl = null;
	    Bitmap imagen = null;
	    try{
	        imageUrl = new URL(imageHttpAddress);
	        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
	        conn.connect();
	        imagen = BitmapFactory.decodeStream(conn.getInputStream());
	    }catch(IOException ex){
	        ex.printStackTrace();
	    }
	     
	    return imagen;
	}
	
	/**
	 * Metodo encargado de crear el directorio que va a contener las Imagenes del Catalogo
	 * 
	 */
	public void crearcarpetaCatalogo() {
		File directory;

		try {
//			sdCard = Environment.getExternalStorageDirectory();
			directory = new File(Util.getDirApp().getPath() + Const.DIR_CATALOGO );
			directory.mkdirs();
			
			directory = new File(Util.getDirApp().getPath() + Const.DIR_CATALOGO_SMALL );
			directory.mkdirs();
			
			directory = new File(Util.getDirApp().getPath() + Const.DIR_CATALOGO_MEDIUM  );
			directory.mkdirs();
			
			directory = new File(Util.getDirApp().getPath() + Const.DIR_CATALOGO_LARGE  );
			directory.mkdirs();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
