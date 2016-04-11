package com.grability.PruebaGrability;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.grability.BusinessObject.DataBaseBO;
import com.grability.Conection.RespSync;
import com.grability.Conection.UtilSync;
import com.grability.Fragment.CatalogoFragment;
import com.grability.Fragment.CategoriaFragment;
import com.grability.Fragment.InformacionAppFragment;
import com.grability.Fragment.NavigationDrawerFragment;
import com.grability.Tool.Const;
import com.grability.Tool.Util;

/**
 * Clase Principal de la aplicacion
 * 
 * @author ASUS
 *
 */
public class MainActivity extends CustomActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, RespSync {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	/**
	 * Progress dialog para mostrar los mensajes de actualizacion
	 */
	private ProgressDialog progressDialog;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	/**
	 * Posicion actual del Fragmento
	 */
	private int posicionActual = 0;

	private Bundle bundleCategorias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!DataBaseBO.existeDataBase()){
			sincronizarDatos();
		}
		
		iniciarFragments();
	}
	
	/**
	 * Metodo que inicializa el NavigationDrawerFragment
	 */
	private void iniciarFragments() {
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		// drawer.setScrimColor(Color.TRANSPARENT);
		drawer.setDrawerShadow(android.R.color.transparent, Gravity.START);
		
	}

	/**
	 * Metodo que sincroniza los datos de l aplicacion
	 */
	private void sincronizarDatos() {
		// TODO Auto-generated method stub
		
		//se muestrael progressdialog para sincronizar la informacion
		mostrarProgressDialog();

		// Se obtiene el Bundle
		Bundle bundle = getIntent().getExtras();

		// Boolean que indica si se puede cargar el Json
		boolean cargarJson = true;
		if (bundle != null) {

			// Se valida si el Extra de Error es contenido
			if (bundle.containsKey("error")) {
				cargarJson = false;
			}
		}

		// Se verifica si no fue un error de la aplicacion para cargar el
		// archivo Json
		if (cargarJson) {

			UtilSync sync = new UtilSync(MainActivity.this, Const.SYNC_PRUEBA, this);
			sync.start();
		}
	}
	
	/**
	 * 
	 */
	public void mostrarProgressDialog() {

		progressDialog = ProgressDialog.show(((Activity) getActionBar().getThemedContext()), "", getResources().getString(R.string.msj_cargado_informacion), true);
		progressDialog.show();			
		
	}
	
	private void ocultarProgressDialog() {
		// TODO Auto-generated method stub
		
//		((MainActivity)((Activity) getActionBar().getThemedContext())).ocultarProgressDialog
		
		if (progressDialog != null)
			progressDialog.cancel();
		
	}

	/**
	 * Metodo para abrir Framentos de manera manual
	 * @param position
	 */
	public void abrirFragmento(int position, Bundle paramArg) {

		FragmentManager fragmentManager = getSupportFragmentManager();
//		fragmentManager.beginTransaction().setCustomAnimations(R.anim.up_from_bottom, R.anim.up_from_bottom);

		switch (position) {

		case Const.POSICION_OPCION_CATEGORIAS:

			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							CategoriaFragment.newInstance(position, paramArg))
					.commit();
			posicionActual = Const.POSICION_OPCION_CATEGORIAS;
			mNavigationDrawerFragment.setCheckedItem(posicionActual);
			
			break;

		case Const.POSICION_OPCION_CATALOGO_APP:
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							CatalogoFragment.newInstance(position, paramArg))
					.commit();
//
			posicionActual = Const.POSICION_OPCION_CATALOGO_APP;
			mNavigationDrawerFragment.setCheckedItem(posicionActual);
			
			//si el Bundle no es null, viene de las categorias, lo guardamos
			//para por si se ejecuta el onKeyDow desde Informacion APP
			bundleCategorias = paramArg;
			
			break;

		case Const.POSICION_OPCION_INFO_APP:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.container,
							InformacionAppFragment.newInstance(position,
									paramArg)).commit();

			posicionActual = Const.POSICION_OPCION_INFO_APP;
			mNavigationDrawerFragment.setCheckedItem(posicionActual);
			break;
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments

		FragmentManager fragmentManager = getSupportFragmentManager();

		switch (position) {
		
		case Const.POSICION_OPCION_CATEGORIAS:
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							CategoriaFragment.newInstance(position, null))
					.commit();

			posicionActual = Const.POSICION_OPCION_CATEGORIAS;
			break;
		
		case Const.POSICION_OPCION_CATALOGO_APP:
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							CatalogoFragment.newInstance(position, null))
					.commit();

			posicionActual = Const.POSICION_OPCION_CATALOGO_APP;
			break;
			
		case Const.POSICION_OPCION_SALIR:

			mNavigationDrawerFragment.selectItem(posicionActual);

			alertCerrarSession();
			break;
		}
	}

	/**
	 * Metodo encargado de arrojar una alerta al usuario para verificar si desea
	 * cerrar session
	 */
	public void alertCerrarSession() {

		AlertDialog alertDialog;

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setCancelable(false).setPositiveButton("SI",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
						finish();
					}
				});

		builder.setCancelable(false).setNegativeButton("NO",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		alertDialog = builder.create();
		alertDialog.setMessage(getString(R.string.msj_cerrar_app));
		alertDialog.show();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onClickGestionFragments();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Metodo encargado de gestionar las las posiciones y motrar fragmentos
	 * cuando es llamado del evento onKeyDown
	 */
	public void onClickGestionFragments() {

		int posicionFragment = obtenerPosicionFragmentVisible();

		switch (posicionFragment) {

		case Const.POSICION_OPCION_CATEGORIAS:
			alertCerrarSession();
			break;

		case Const.POSICION_OPCION_CATALOGO_APP:
			abrirFragmento(Const.POSICION_OPCION_CATEGORIAS, null);
			break;

		case Const.POSICION_OPCION_INFO_APP:
			abrirFragmento(Const.POSICION_OPCION_CATALOGO_APP, bundleCategorias);
			break;

		default:
			break;
		}
	}

	/**
	 * Metodo encargado de obtener la posicion actual del fragment visible
	 * 
	 * @return
	 */
	public int obtenerPosicionFragmentVisible() {

		int posicion = -1;
		Fragment fragment = getVisibleFragment();
		if (fragment != null) {
			if (fragment.getArguments() != null)
				posicion = fragment.getArguments()
						.getInt(Const.FRAGMENT_ACTUAL);

		}

		return posicion;
	}

	/**
	 * Obtener el fragment visible
	 * 
	 * @return
	 */
	public Fragment getVisibleFragment() {
		
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		List<Fragment> fragments = fragmentManager.getFragments();
		for (Fragment fragment : fragments) {
			if (fragment != null && fragment.isVisible())
				return fragment;
		}
		return null;
	}

	/**
	 * Metodo que selecciona el Fragmento
	 * @param number
	 */
	public void onSectionAttached(int number) {
		switch (number) {
		case Const.POSICION_OPCION_CATEGORIAS:
			mTitle = getString(R.string.categorias);
			restoreActionBar();
			break;

		case Const.POSICION_OPCION_CATALOGO_APP:
			mTitle = getString(R.string.aplicaciones);
			restoreActionBar();
			break;

		case Const.POSICION_OPCION_INFO_APP:
			mTitle = getString(R.string.info_app);
			restoreActionBar();
			break;
		}
	}

	/**
	 * Metodo encargado de refrescar la informacion del fragment
	 */
	public void refrescarFragmentActual() {

		Fragment fragment = getVisibleFragment();
		if (fragment != null) {

			int fragmentActual = fragment.getArguments().getInt(
					Const.FRAGMENT_ACTUAL);

			switch (fragmentActual) {
			case Const.POSICION_OPCION_CATEGORIAS:

				CategoriaFragment CFragment = (CategoriaFragment) fragment;
				CFragment.cargarInfoGeneral();
				break;

			case Const.POSICION_OPCION_CATALOGO_APP:
				CatalogoFragment CatFragment = (CatalogoFragment) fragment;
				CatFragment.cargarInfoGeneral();
				break;
				
			case Const.POSICION_OPCION_INFO_APP:
				InformacionAppFragment InfFragment = (InformacionAppFragment) fragment;
				InfFragment.cargarInfoGeneral();
				break;

			}

		}
	}

	/**
	 * Metodo que restaura la posicion del ActionBar
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.activity_main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Metodo encargado de controlar los eventos del menu de la barra de
	 * acciones
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.sincronizar:
			
			sincronizarDatos();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Metodo encargado de actualizar y mostrar un dialogo del resultado de la sincronizacion
	 * @param estado
	 * @param respuestaServer
	 * @param codeRespuesta
	 * @param context
	 */
	public void respuestaDownloadInfo(final boolean estado, final String respuestaServer, int codeRespuesta,final Context context) {
		
		this.runOnUiThread(new Runnable() {
			
			public void run() {
				
				//se oculta el progress dialog
				ocultarProgressDialog();
				
				//Se obtiene el color a mostrar en el dialogo
				String color = getResources().getString(R.color.naranja);
				
				//Se obtiene el titulo del diaogo
				String titulo = getResources().getString(R.string.titulo_sync);
				
				//se muestra el dialogo
				Util.mostrarAlertDialog(titulo, respuestaServer, color , context);
				//se refrescan los elementos con la informacion obtenida
				refrescarFragmentActual();

			}
		});
	}
	
	@Override
	public void respSync(boolean estado, String msg, int codeRequest) {
		// TODO Auto-generated method stub

		switch (codeRequest) {

		case Const.SYNC_PRUEBA:
			respuestaDownloadInfo(estado, msg, codeRequest, this);

			break;
		}
	}
}
