package com.grability.PruebaGrability;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.grability.PruebaGrability.R;

/**
 * @author 
 *
 */
public class CustomActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
    	/*Se agrega para capturar cualquier excepcion en cualquier actividad que extienda de CustomActivity */ 
    	Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }
	
	
    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(int)
     */
    @Override
    public void setContentView(int layoutResID) {
    	
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    	super.setContentView(layoutResID);
    	
    	
    	/*
    	 * SE identifica si el dispositivo es mayor a la densidad de 600dp (tablet) y se orienta la pantalla modo PORTRAIT
    	 * como se definio en el requerimiento
    	 */
        if(getResources().getBoolean(R.bool.portrait_only))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
    }

}
