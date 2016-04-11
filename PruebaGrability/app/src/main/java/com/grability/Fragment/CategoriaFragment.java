package com.grability.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.grability.Adapter.ListViewAdapter;
import com.grability.BusinessObject.DataBaseBO;
import com.grability.DataObject.Category;
import com.grability.DataObject.ItemListView;
import com.grability.PruebaGrability.MainActivity;
import com.grability.PruebaGrability.R;
import com.grability.Tool.Const;

public class CategoriaFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";
	private ArrayList<Category> listaCategorias;

	/**
	 * Contructur del fragment
	 */
	public CategoriaFragment() {
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
		View rootView = inflater.inflate(R.layout.fragment_categoria, 	container, false);

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
	public static CategoriaFragment newInstance(int sectionNumber, Bundle paramArgs) {
		CategoriaFragment fragment = new CategoriaFragment();
		
		if(paramArgs == null)
		 paramArgs = new Bundle();
		
		paramArgs.putInt(ARG_SECTION_NUMBER, sectionNumber);
		paramArgs.putInt(Const.FRAGMENT_ACTUAL, Const.POSICION_OPCION_CATEGORIAS);
		fragment.setArguments(paramArgs);
		return fragment;
	}

	/**
	 * Metodo encargado de cargar la informacion general
	 */
	public void cargarInfoGeneral() {
		
		//Se valida si exise la base de datos
		if(DataBaseBO.existeDataBase())
			cargarListaCategoias();
	}


	private void cargarListaCategoias() {
		
		LinearLayout vistaFragment = (LinearLayout) ((Activity) getActionBar()
				.getThemedContext()).findViewById(R.id.fragmentCategoria);
		
		if (vistaFragment != null) {
		
			Activity context = ((Activity) getActionBar().getThemedContext());
	//		int heigth = (Integer.parseInt(context.getResources().getString(R.string.tamanio_imagen)));
			
			ListView listViewCategorias = (ListView) vistaFragment.findViewById(R.id.listViewCategorias);
			
		    List<ItemListView> listItems = new ArrayList<ItemListView>();
		    listaCategorias = DataBaseBO.getListaCategorias(listItems);
		    
//		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);
//		    listViewCategorias.setAdapter(adapter);
		    
		    ListViewAdapter adapter = new ListViewAdapter(context, listItems, R.drawable.ic_launcher, context.getResources().getColor(R.color.negro));
		    listViewCategorias.setAdapter(adapter);
		    
		    
		    //Se agrega el evento OnItemClickListener del listViewCategorias
		    listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            
				@Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
					// Se crea un nuevo Bundle donde va a ir la informacion seleccionada
					Bundle bundle = null;
					
					//Se verifica que la lista no este null
					if(listaCategorias != null){
						//SE selecciona el Objet de la lista, dependiendo de la posicion seleccionada
						Category category = listaCategorias.get(position);
						
						//se instancia el Bundle
						bundle = new Bundle();
						//se Agrega la informacion al Bundle que vamos a enviar
						bundle.putString(Const.BUNDLE_CATEGORY, category.getIdCategoria());
					}
					
					//Se ejecuta el metodo para abrir otro fragmento, con su respectivo Bundle
					((MainActivity) getActionBar().getThemedContext()).abrirFragmento(Const.POSICION_OPCION_CATALOGO_APP, bundle);
	               
				}
	        });
		}
	}
	

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
