package nmt.turkistikyerler;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener,ActionBar.TabListener{

	public List<String> list_parent;
	public ExpandListViewAdapter expand_adapter;
	public HashMap<String, List<String>> list_child;
	public ExpandableListView expandlist_view;
	public TextView txt_icerik;

	public int last_position = -1;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private String grupAdi;
	private String alanAdi;
	private String turAdi;
	private String aciklamaAdi;
	ListView liste;
	Boolean childKontrol = false , konumKontrol = false;

	private ViewPager viewPager;
	private tabGecisAdapter tabGecisAdapter;
	private ActionBar actionBar;

	private int lastExpandedPosition = -1;

	public List<String> ALGezYer;
	public List<String> ALOtel;
	public List<String> ALMekan;

    int gezYerSayi = 0,mekSayi = 0,otelSayi = 0;

	String menuList[];
	String gezilecekYerler[];
	String oteller[];
	String mekanlar[];

	Cursor crs;

	DatabaseHelper dbHelper;
    konumCek konumcek;

	LocationManager location_manager;

	double x;
	double y;

	Geocoder geocoder;
	List<Address> addresses;

	private String[] tabs = {"LİSTE","BİLGİLENDİRME"};

	FragmentListe fragmentListe;

	ProgressDialog progressDialog;


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Veriler Alınıyor");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();


		//Veritabanı

		dbHelper = new DatabaseHelper(this);
		try {
			dbHelper.createDataBase();
			dbHelper.openDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}

		location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		final LocationListener listner = new MyLocationListner();

		location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listner);


		//Tab İşlemleri
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		tabGecisAdapter = new tabGecisAdapter(getSupportFragmentManager());

		viewPager.setAdapter(tabGecisAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//tab ekleme
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		//tab ikon ayarlama
		//actionBar.getTabAt(0).setIcon(R.drawable.liste);

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {

				actionBar.setSelectedNavigationItem(position);

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		expandlist_view = (ExpandableListView) findViewById(R.id.expand_listview);


		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		menuList = getResources().getStringArray(R.array.menuList);
		gezilecekYerler = getResources().getStringArray(R.array.gezilecekYerler);
		oteller = getResources().getStringArray(R.array.oteller);
		mekanlar = getResources().getStringArray(R.array.mekanlar);

		Hazirla(); // expandablelistview i�eri�ini haz�rlamak i�in

		// Adapter s�n�f�m�z� olu�turmak i�in ba�l�klardan olu�an listimizi ve onlara ba�l� olan elemanlar�m�z� olu�turmak i�in HaspMap t�r�n� yolluyoruz
		expand_adapter = new ExpandListViewAdapter(getApplicationContext(), list_parent, list_child);
		expandlist_view.setAdapter(expand_adapter);  // olu�turdu�umuz adapter s�n�f�n� set ediyoruz
		expandlist_view.setClickable(true);

		expandlist_view.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition, long id) {

				childKontrol = true;
				String child_name = (String) expand_adapter.getChild(groupPosition, childPosition);
				String parent_name = (String) expand_adapter.getGroup(groupPosition);

				setGrupAdi(parent_name);

				final ArrayList<String> txt = new ArrayList<String>();

				crs = dbHelper.getDatabase().query(getGrupAdi(), new String[]{getAlanAdi()}, getTurAdi() + " = ?", new String[]{child_name.toString()}, null, null, null);

				while (crs.moveToNext()) {  //döngü ile veritabanindan çekilen kolonlarin degiskenlere aktarilmasi
					String bilgi = crs.getString(crs.getColumnIndex(getAlanAdi()));
					txt.add(bilgi);
				}
				ArrayAdapter<String> gezAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, txt);
				liste.removeAllViewsInLayout();
				liste.setAdapter(gezAdapter);


				drawerLayout.closeDrawers();
				viewPager.setCurrentItem(0);
				if (konumKontrol == false) {
					location_manager.removeUpdates(listner);
					location_manager = null;
					konumKontrol = true;
				}
				return true;

			}


		});

		//Bir parent tıklandığında diğerleri kapansın
		expandlist_view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				if (lastExpandedPosition != -1
						&& groupPosition != lastExpandedPosition) {
					expandlist_view.collapseGroup(lastExpandedPosition);
				}
				lastExpandedPosition = groupPosition;
			}
		});

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {//men� a�l�p kapand���nda action bar title de�i�imi
			//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerClosed(View view) { //ac�kken uygulama ad� g�z�kecek
				getActionBar().setTitle("TÜRKİSTİK YERLER");
				// calling onPrepareOptionsMenu() to show action bar icons
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerOpened(View drawerView) {//kapalk�yken men� ad�
				getActionBar().setTitle("TÜRKİSTİK YERLER");
				super.onDrawerOpened(expandlist_view);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		actionBar.setDisplayHomeAsUpEnabled(true);


	}

	public Boolean yerKontrol = false;

	public void setYerKontrol(Boolean yerKontrol){
		this.yerKontrol=yerKontrol;
	}

	public Boolean getYerKontrol(){
		return yerKontrol;
	}


	public void setGrupAdi(String grupAdi){
		if(grupAdi.equals("GEZİLECEK YERLER")) {
			this.grupAdi = "gezilecek_yerler";
			this.alanAdi="yer_adi";
			this.turAdi="yer_turu";
			this.aciklamaAdi="aciklama";
		}

		else if(grupAdi.equals("OTELLER")) {
			this.grupAdi = "oteller";
			this.alanAdi="otel_adi";
			this.turAdi="otel_turu";
			this.aciklamaAdi="otel_aciklama";
		}

		else if(grupAdi.equals("MEKANLAR")) {
			this.grupAdi = "mekanlar";
			this.alanAdi = "mekan_adi";
			this.turAdi="mekan_turu";
			this.aciklamaAdi="mekan_aciklama";
		}

		}
	public String getAlanAdi(){return alanAdi;}
	public String getGrupAdi(){return grupAdi;}
	public String getTurAdi(){return turAdi;}
	public String getAciklamaAdi(){return aciklamaAdi;}


	//Konum bul veritabanına bağlantı yap
	String iladi;
	public void setIlAyarla(String il)
	{
		iladi=il;
	}
	public String getİlAdi()
	{
		return iladi;
	}

	public class MyLocationListner implements LocationListener {


		public void onLocationChanged(Location location) {

            konumcek = (konumCek)getApplicationContext();
			String sehir;

			x = location.getLatitude();
			y = location.getLongitude();


			try {

				geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
				addresses = geocoder.getFromLocation(x, y, 1);


					if (addresses.size() > 0 && addresses != null) {

						sehir = addresses.get(0).getAdminArea();
						setIlAyarla(sehir);

					}


				liste= (ListView) findViewById(R.id.listeYerler);

					crs = dbHelper.getDatabase().query("sehirler", new String[]{"il_id"}, "il_adi = ?", new String[]{getİlAdi().toString().toLowerCase()}, null, null, null);

					final ArrayList<String> txt = new ArrayList<String>();//array list (
					final ArrayList<Long> idtxt = new ArrayList<Long>();



					if (crs.moveToNext()) {  //döngü ile veritabanindan çekilen kolonlarin degiskenlere aktarilmasi

						long id = crs.getLong(crs.getColumnIndex("il_id"));
						idtxt.add(id);
					}

					crs = dbHelper.getDatabase().query("gezilecek_yerler", new String[]{"yer_adi"}, "il_id = ?", new String[]{idtxt.get(0).toString()}, null, null, null);

					while (crs.moveToNext()) {  //döngü ile veritabanindan çekilen kolonlarin degiskenlere aktarilmasi
						String bilgi = crs.getString(crs.getColumnIndex("yer_adi"));
						txt.add(bilgi);
						gezYerSayi++;
					}
					crs = dbHelper.getDatabase().query("oteller", new String[]{"otel_adi"}, "il_id = ?", new String[]{idtxt.get(0).toString()}, null, null, null);

					while (crs.moveToNext()) {  //döngü ile veritabanindan çekilen kolonlarin degiskenlere aktarilmasi
						String bilgi = crs.getString(crs.getColumnIndex("otel_adi"));
						txt.add(bilgi);
						otelSayi = gezYerSayi;
						otelSayi++;
					}
					crs = dbHelper.getDatabase().query("mekanlar", new String[]{"mekan_adi"}, "il_id = ?", new String[]{idtxt.get(0).toString()}, null, null, null);

					while (crs.moveToNext()) {  //döngü ile veritabanindan çekilen kolonlarin degiskenlere aktarilmasi
						String bilgi = crs.getString(crs.getColumnIndex("mekan_adi"));
						txt.add(bilgi);
						mekSayi = otelSayi;
						mekSayi++;
					}

					ArrayAdapter<String> gezAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, txt);
					liste.setAdapter(gezAdapter);
					progressDialog.cancel();

				liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


						if (childKontrol == false) {
							if (liste.getSelectedItemId() <= gezYerSayi)
								setGrupAdi("GEZİLECEK YERLER");

							else if (liste.getSelectedItemId() > gezYerSayi && liste.getSelectedItemId() <= otelSayi)
								setGrupAdi("OTELLER");

							else
								setGrupAdi("MEKANLAR");
						}

						txt_icerik = (TextView) findViewById(R.id.txt_icerik);

						crs = dbHelper.getDatabase().query(getGrupAdi(), new String[]{getAlanAdi(),getAciklamaAdi(),"knm_x","knm_y"}, getAlanAdi() + " = ?", new String[]{liste.getItemAtPosition(position).toString()}, null, null, null);



						while (crs.moveToNext()) {
							txt_icerik.setText(crs.getString(crs.getColumnIndex(getAciklamaAdi())));
                            konumcek.setKonumX(crs.getFloat(crs.getColumnIndex("knm_x")));
                            konumcek.setKonumY(crs.getFloat(crs.getColumnIndex("knm_y")));
                            konumcek.setYerAdi(crs.getString(crs.getColumnIndex(getAlanAdi())));
						}
						txt_icerik.setMovementMethod(new ScrollingMovementMethod());

						setYerKontrol(true);

						viewPager.setCurrentItem(1);
					}
				});

				Toast.makeText(getApplicationContext(),"Konumuz Belirlendi", Toast.LENGTH_SHORT).show();

			} catch (IOException e) {

				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

			}

		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

			final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(MainActivity.this);

			// Setting Dialog Title
			mAlertDialog.setTitle("Konumunuz etkin değil")
					.setMessage("Lütfen Konum Ayarlarınızı Değiştirin")
					.setCancelable(false)
					.setPositiveButton("Ayarlar", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

							startActivity(intent);
						}
					})
					.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							finish();

						}
					}).show();
		}
	}

	//ActionBar a tıklandığında kayan menüyü aç/kapa
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(drawerToggle.onOptionsItemSelected(item))
			return true;

		switch (item.getItemId()){
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		drawerToggle.onConfigurationChanged(newConfig);
	}

	public void Hazirla()
	{
		list_parent = new ArrayList<String>();  //kayan menü ana başlıklar
		list_child = new HashMap<String, List<String>>(); //başlıkların alt öğeleri

		ALGezYer = new ArrayList<String>();
		for(int i = 0; i < gezilecekYerler.length; i++)
			ALGezYer.add(gezilecekYerler[i]);

		ALOtel = new ArrayList<String>();
		for(int i = 0; i < oteller.length; i++)
			ALOtel.add(oteller[i]);

		ALMekan = new ArrayList<String>();
		for(int i = 0; i < mekanlar.length; i++)
			ALMekan.add(mekanlar[i]);

		list_parent.add(menuList[0]);
		list_parent.add(menuList[1]);
		list_parent.add(menuList[2]);

		list_child.put(menuList[0], ALGezYer);
		list_child.put(menuList[1], ALOtel);
		list_child.put(menuList[2], ALMekan);

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

	}

	//Geri tuşuna baıldığında
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Uyarı!!");
		builder.setMessage("Çıkış Yapacak mısın?");
		builder.setCancelable(false);

		builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog dialog =builder.create();
		dialog.show();

		return super.onKeyDown(keyCode, event);
	}
}
