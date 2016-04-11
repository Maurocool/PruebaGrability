package com.grability.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.grability.BusinessObject.DataBaseBO;
import com.grability.DataObject.Entry;
import com.grability.PruebaGrability.MainActivity;
import com.grability.PruebaGrability.R;
import com.grability.Tool.Const;
import com.grability.Tool.Util;

public class InformacionAppFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";
	private String idImagen = "";

	/**
	 * Contructur del fragment
	 */
	public InformacionAppFragment() {
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
		View rootView = inflater.inflate(R.layout.fragment_informacion_app, container, false);

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
	public static InformacionAppFragment newInstance(int sectionNumber, Bundle paramArgs) {
		InformacionAppFragment fragment = new InformacionAppFragment();
		
		if(paramArgs == null)
		 paramArgs = new Bundle();
		
		paramArgs.putInt(ARG_SECTION_NUMBER, sectionNumber);
		paramArgs.putInt(Const.FRAGMENT_ACTUAL, Const.POSICION_OPCION_INFO_APP);
		fragment.setArguments(paramArgs);
		return fragment;
	}

	/**
	 * Metodo encargado de cargar la informacion general
	 */
	public void cargarInfoGeneral() {
		
		//Se obtienen los argumentos del Bundle
		Bundle bundle = this.getArguments();
		
		// se valida que el Bundle no sea null
		if(bundle != null){
			
			//se verifica si contiene el atributo esperado y de ser asi lo obtiene
			if(bundle.containsKey(Const.BUNDLE_IMAGEN))
				idImagen  = bundle.getString(Const.BUNDLE_IMAGEN);
		}

		LinearLayout vistaFragment = (LinearLayout) ((Activity) getActionBar()
				.getThemedContext()).findViewById(R.id.fragmentInformacionApp);

		//Se obtiene la imagen de mayor tama o ya que va a ser mostrada en el framento que va a contener toda la informacion
		//de la APlicacion seleccionada
			Entry entry = DataBaseBO.getEntry(idImagen, Const.IMAGE_LARGE);
		
			//SE valida que ni el fragmento ni el objeto Entry sean nulos
			if (vistaFragment != null && entry != null) {
				
				//Se procede a settar la informacion del fragmento

				((TextView) vistaFragment.findViewById(R.id.txtDescripcion)).setText(entry.getSummary());
				((TextView) vistaFragment.findViewById(R.id.txtCategoria)).setText(entry.getCategory());
				((TextView) vistaFragment.findViewById(R.id.txtLiberado)).setText(entry.getImReleaseDate());
				((TextView) vistaFragment.findViewById(R.id.txtDerechos)).setText(entry.getRights());
				((TextView) vistaFragment.findViewById(R.id.txtPrice)).setText(entry.getImPrice() + " " + entry.getCurrency());
				
				ImageView imageViewInfoApp = ((ImageView) vistaFragment.findViewById(R.id.imageViewInfoApp));
				
				//Se obtieje el contexto de la app
				Activity context = ((Activity) getActionBar().getThemedContext());

				//Se actualiza la imagen del ImageView del fragmento
				Util.actualizarImagenCircular(imageViewInfoApp, entry.getUrlImagen(), context, Const.RESIZE_IMAGE_INFO);
				
				//Se setea el valor del titulo
				((TextView) vistaFragment.findViewById(R.id.txtTitulo)).setText(entry.getTitle());
				
				//Se crea una animacion para la imagen 
		        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		        imageViewInfoApp.startAnimation(animation);
		        
		        //Se obtiene el ScrollView del fragmento
		        ScrollView ScrollViewInfoApp = ((ScrollView) vistaFragment.findViewById(R.id.ScrollViewInfoApp));
		        
				//Se crea una animacion para la el contenedor  ScrollView
		        Animation animationText = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
		        ScrollViewInfoApp.startAnimation(animationText);
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
