package com.grability.Fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.grability.Adapter.GridViewAdapter;
import com.grability.BusinessObject.DataBaseBO;
import com.grability.DataObject.Imagen;
import com.grability.PruebaGrability.MainActivity;
import com.grability.PruebaGrability.R;
import com.grability.Tool.Const;
import com.grability.Tool.Util;

public class CatalogoFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";
	
	private GridView gridview;
	private ArrayList<Imagen> listaImagenes;


	/**
	 * Constructor del fragment
	 */
	public CatalogoFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	/**
	 * Metodo enl cual se asocia el xml al fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_catalogo, 	container, false);

		return rootView;
	}

	/**
	 * Se realiza la carga de componentes despues se que se hubiera implementado
	 * la carga del xml
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		cargarInfoGeneral();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static CatalogoFragment newInstance(int sectionNumber, Bundle paramArgs) {
		CatalogoFragment fragment = new CatalogoFragment();
		
		if(paramArgs == null)
			 paramArgs = new Bundle();
		
		paramArgs.putInt(ARG_SECTION_NUMBER, sectionNumber);
		paramArgs.putInt(Const.FRAGMENT_ACTUAL, Const.POSICION_OPCION_CATALOGO_APP);
		fragment.setArguments(paramArgs);
		return fragment;
	}

	/**
	 * Metodo encargado de cargar la informacion general
	 */
	public void cargarInfoGeneral() {
		
		//Se valida si exise la base de datos
		if(!DataBaseBO.existeDataBase()){
			
			//Se obtiene el contexto de la aplicacion
			Activity context = ((Activity) getActionBar().getThemedContext());
			
			// se obtiene el titulo para el mensaje
			String titulo = context.getResources().getString(R.string.titulo_falta_inf);
			// se obtiene el mensaje
			String texto = context.getResources().getString(R.string.msj_error_falta_info);
			// se obtiene el color para el mensaje
			String color = context.getResources().getString(R.color.rojo);
			
			Util.mostrarAlertDialog(titulo, texto, color, context);
			
			//retorna ya que no hay informacion para mostrar
			return;
		}
		
		String idCategoriaSeleccionada = "";
		
		//Se obtienen los argumentos del Bundle
		Bundle bundle = this.getArguments();
		
		// se valida que el Bundle no sea null
		if(bundle != null){
			
			//se verifica si contiene el atributo esperado y de ser asi lo obtiene
			if(bundle.containsKey(Const.BUNDLE_CATEGORY))
				 idCategoriaSeleccionada  = bundle.getString(Const.BUNDLE_CATEGORY);
		}

		//Se Crea una instancia de la Clase CargarImagene
		CargarImagenes ci = new CargarImagenes(idCategoriaSeleccionada);
		//Se llama al metodo start ya que la clase extiende de un hilo
		ci.start();
	}
	
	
	/**
	 * Clase que extiende de un hilo para cargar las imagenes
	 * dependiendo de la categoria seleccionada
	 * @author ASUS
	 */
	class CargarImagenes extends Thread {
		
		private String idCategoriaSeleccionada;

		public CargarImagenes(String idCategoriaSeleccionada) {
			// TODO Auto-generated constructor stub
			this.idCategoriaSeleccionada = idCategoriaSeleccionada;
		}
		
		public void run() {
			
			//Se obtiene el contexto de la aplicacion
			Activity context = ((Activity) getActionBar().getThemedContext());
			//se obtiene el tama o de la imagen dependiendo del dispositivo
			int heigth = (Integer.parseInt(context.getResources().getString(R.string.tamanio_imagen)));
			
			//se obtienen las imagenes con la categoria seleccionada
			listaImagenes = DataBaseBO.getListaImagenes(idCategoriaSeleccionada, Integer.parseInt(Const.IMAGE_LARGE));
			
			//se crea una instanacia de GridViewAdapter que va a contener las imagenes
			final GridViewAdapter adapter = new GridViewAdapter(context, listaImagenes, heigth);;
			
			context.runOnUiThread(new Runnable() {
				
				public void run() {
					//se cargan las imagenes
					cargarImagenesGridView(adapter);
				}
			});
		}
	}

	/**
	 * Mtodo encargado de settear el adapter crear el evento en el Gridview
	 * @param adapter
	 */
	protected void cargarImagenesGridView(GridViewAdapter adapter) {
		// TODO Auto-generated method stub
		LinearLayout vistaFragment = (LinearLayout) ((Activity) getActionBar()
				.getThemedContext()).findViewById(R.id.fragmentCatalogo);
		
		//Se valida que el fragmento no sea nulo
		if(vistaFragment != null){
		
			gridview = (GridView) vistaFragment.findViewById(R.id.gvImagenes);// crear el
			// gridview a partir del elemento del xml gridview
			gridview.setAdapter(adapter);
	
			gridview.setOnItemClickListener(new OnItemClickListener() {
	
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					
					// Se crea un nuevo Bundle donde va a ir la informacion seleccionada
					Bundle bundle = null;
					
					//Se verifica que la lista no este null
					if(listaImagenes != null){
						//SE selecciona el Objet de la lista, dependiendo de la posicion seleccionada
						Imagen imagen = listaImagenes.get(position);
						
						//se instancia el Bundle
						bundle = new Bundle();
						//se Agrega la informacion al Bundle que vamos a enviar
						bundle.putString(Const.BUNDLE_IMAGEN, imagen.getIdImagen());
					}
					
					//Se ejecuta el metodo para abrir otro fragmento, con su respectivo Bundle
					((MainActivity) getActionBar().getThemedContext()).abrirFragmento(Const.POSICION_OPCION_INFO_APP, bundle);
				}
			});
		}
	}


	/**
	 * metodo encargado de obtener el ActionBar
	 * @return
	 */
	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		cargarInfoGeneral();
	}
}
