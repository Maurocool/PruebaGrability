package com.grability.Tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

import com.grability.PruebaGrability.R;

public class Util {
	
	private final static String TAG = Util.class.getName();
	
	public static String obtenerImei(Context context) {
		
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
	}
	
	/**
	 * @return 
	 */
	public static File getDirApp() {
		
		File SDCardRoot = Environment.getExternalStorageDirectory();
		File dirApp = new File(SDCardRoot.getPath() + "/" + Const.NAME_DIR_APP);
		

		if(!dirApp.isDirectory())
			dirApp.mkdirs();
		
		return dirApp;
	}
	
	/**
	 * Metodo que setea una imagen obtenida por url interna en al aplicacion
	 * en un ImageView
	 * @param image
	 * @param urlImagen
	 * @param context
	 */
	public static void actualizarImagen(ImageView image, String urlImagen, Context context, int multipSize) {
		
		String direccionGnral = getDirApp().getPath() + Const.DIR_CATALOGO + "/"; 
		String direccion = urlImagen;
		File imgFile = new File( direccion );

		if(imgFile.exists()){

    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    	    
    	    if( myBitmap != null ){
    	    	
				//se obtiene el tama o de la image dependiento del dispositivo
				int size = (Integer.parseInt(context.getResources().getString(R.string.tamanio_imagen))) * multipSize;
    	    	
    	    	myBitmap = getResizedBitmap(imgFile.getPath(), myBitmap, size, size );
    	    }
    	    image.setImageBitmap(myBitmap);
    	}
		else{
			
			String nombreImagen = getNombreImagen( urlImagen, "" );
			
			if( !nombreImagen.equals( "" ) ){
				
				direccion = direccionGnral + nombreImagen;
				imgFile = new File( direccion );

				if(imgFile.exists()){

		    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	    	    	
					//se obtiene el tama o de la image dependiento del dispositivo
					int size = (Integer.parseInt(context.getResources().getString(R.string.tamanio_imagen))) * multipSize;
	    	    	
	    	    	myBitmap = getResizedBitmap(imgFile.getPath(), myBitmap, size, size );
	    	    	
		    	    image.setImageBitmap(myBitmap);
		    	}
			}
		}
	}
	
	/**
	 * Metodo que setea una imagen circula obtenida por url interna en al aplicacion
	 * en un ImageView
	 * @param image
	 * @param urlImagen
	 * @param context
	 */
	public static void actualizarImagenCircular(ImageView image, String urlImagen, Context context, int multipSize) {
		
		String direccionGnral = getDirApp().getPath() + Const.DIR_CATALOGO + "/"; 
		String direccion = urlImagen;
		File imgFile = new File( direccion );

		if(imgFile.exists()){

    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    	    
    	    if( myBitmap != null ){
    	    	
				//se obtiene el tama o de la image dependiento del dispositivo
				int size = (Integer.parseInt(context.getResources().getString(R.string.tamanio_imagen))) * multipSize;
    	    	
    	    	myBitmap = getResizedBitmap(imgFile.getPath(), myBitmap, size, size );
    	    }
    	    
    	    //creamos el drawable redondeado
			RoundedBitmapDrawable roundedDrawable =
    	            RoundedBitmapDrawableFactory.create(context.getResources(), myBitmap);
    	 
    	    //asignamos el CornerRadius
    	    roundedDrawable.setCornerRadius(myBitmap.getHeight());
    	    image.setImageDrawable(roundedDrawable);
    	    
    	    
//    	    image.setImageBitmap(myBitmap);
    	}
		else{
			
			String nombreImagen = getNombreImagen( urlImagen, "" );
			
			if( !nombreImagen.equals( "" ) ){
				
				direccion = direccionGnral + nombreImagen;
				imgFile = new File( direccion );

				if(imgFile.exists()){

		    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    	    
//		    	    if( myBitmap != null ){
//		    	    	
//		    	    	myBitmap = getResizedBitmap(imgFile.getPath(), myBitmap, 450, 350 );
//		    	    }
		    	    
		    	    image.setImageBitmap(myBitmap);
		    	}
			}
//			else{ 
//
//				Drawable fotoVacia = context.getResources().getDrawable(R.drawable.sin_imagen);
//				Bitmap bitmap = ((BitmapDrawable)fotoVacia).getBitmap();
//				bitmap = getResizedBitmap(imgFile.getPath(), bitmap, 450, 350 );
//				//Drawable img = Util.ResizedImage(fotoVacia, 300, 150);
//				
//				image.setImageBitmap( bitmap );
//			}
		}
	}
	
	
	public static String getNombreImagen( String codigo, String carpeta ){
		
		String nombreImagen = "", nombImagen = "";
		
		File SDCardRoot = Environment.getExternalStorageDirectory();
		File dirApp;
		
		if( carpeta.equals( "" ) ){
		
			dirApp = new File(SDCardRoot.getPath() + "/" + Const.DIR_CATALOGO);
		}
		else{
			
			dirApp = new File(SDCardRoot.getPath() + "/" + Const.DIR_CATALOGO + "/" + carpeta);
		}
		
		if( dirApp.exists() ){
			
			File[] files;
			
			files = dirApp.listFiles();
			
			for( int i = 0; i < files.length; i++ ){
				
				if( files[ i ].isDirectory() ){
									
					nombreImagen = getNombreImagen( codigo, files[ i ].getName() );
					
					if( !nombreImagen.equals( "" ) )
						break;
				}
				else{
					
					nombImagen = files[ i ].getName();
					
					if( nombImagen.indexOf( codigo ) != -1 ){
						
						if( carpeta.equals( "" ) )
							nombreImagen = nombImagen;
						else
							nombreImagen = carpeta + "/" + nombImagen;
						
						break;
					}
				}			
			}
			
		}
		else{
			
			nombreImagen = "";
		}
		
		return nombreImagen;
	}
	
	
	public static Bitmap getResizedBitmap(String path, Bitmap bm, int newHeight, int newWidth) {
	    
		FileInputStream fd = null;
		int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    
        try {
		    // CREATE A MATRIX FOR THE MANIPULATION
		    Matrix  matrix = new Matrix();
		    // RESIZE THE BIT MAP
		    matrix.postScale(scaleWidth, scaleHeight);
		    
		    try {
		        fd = new FileInputStream(path);
		        BitmapFactory.Options option = new BitmapFactory.Options();
		        option.inDither = false;
		        option.inPurgeable = true;
		        option.inInputShareable = true;
		        option.inTempStorage = new byte[32 * 1024];
		        option.inPreferredConfig = Bitmap.Config.RGB_565;
		       
		        bm = BitmapFactory.decodeFileDescriptor(fd.getFD(), null, option);
		    } catch(Exception e) {}
		        
	
		    // "RECREATE" THE NEW BITMAP
		    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		    return resizedBitmap;
		    
        } catch(Exception e) {
            
            Log.e(TAG, "resizedImage -> " + e.getMessage(), e);
            return null;
           
     } finally {
           
            if (fd != null) {
                 
                  try {
                    
                	  fd.close();
                        
                  } catch (IOException e) { }
            }
     	}
	}

	/**
	 * Metodo que obtiene la ruta de la carpeta dependiendo del tamanio
	 * @param height, tamanio a buscar
	 * @return String, ruta de la carpeta
	 */
	public static String obtenerDirCarpetaImgTamanio(String height) {
		// TODO Auto-generated method stub
		String carpeta = "";
		
		//Se obtiene la carpeta por medio del tama o de la Image
		if(height.equals(Const.IMAGE_SMALL))
			carpeta = Const.DIR_CATALOGO_SMALL;
		else if(height.equals(Const.IMAGE_MEDIUM))
			carpeta = Const.DIR_CATALOGO_MEDIUM;
		else
			carpeta = Const.DIR_CATALOGO_LARGE;
		
		return carpeta;
	}
	
	
	public static void mostrarAlertDialog(String titulo, String texto, String color, Context context){
		
        QustomDialogBuilder qustomDialogBuilder = (QustomDialogBuilder) new QustomDialogBuilder(context).
            setTitle(titulo).
            setTitleColor(color).
            setDividerColor(color).
            setMessage(texto).
            setCancelable(false).
            setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//				
				public void onClick(DialogInterface dialog, int id) {
					
					dialog.cancel();
				}
			});;

        qustomDialogBuilder.show();
	}



}
