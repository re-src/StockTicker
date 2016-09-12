package at.jku.stockticker.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import at.jku.stockticker.R;
import at.jku.stockticker.pojo.Price;
import at.jku.stockticker.pojo.Stock;
import at.jku.stockticker.services.OptionsService;
import at.jku.stockticker.services.PreferencesService;
import at.jku.stockticker.services.PriceService;

public class MainActivity extends Activity {

	private EditText fromDateTxt;
	private EditText toDateTxt;
	private Button stocksBtn;
	private Button savedStocksBtn;
	private Button showBtn;
	
	private SimpleDateFormat sdf;
	
	private Date fromDate;
	private Date toDate;
	
	private List<Stock> selectedStocks;
	private List<Stock> portfolio;
	
	private List<Stock> availableStocks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.fromDateTxt = (EditText) findViewById(R.id.dateFrom);
		this.toDateTxt = (EditText) findViewById(R.id.dateTo);
		this.stocksBtn = (Button) findViewById(R.id.buttonStocks);
		this.savedStocksBtn = (Button) findViewById(R.id.buttonSavedStocks);
		this.showBtn = (Button) findViewById(R.id.buttonShow);
		
		this.sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);	
		this.fromDate = Calendar.getInstance().getTime();
		this.toDate = Calendar.getInstance().getTime();
		this.fromDateTxt.setText(sdf.format(this.fromDate));
		this.toDateTxt.setText(sdf.format(this.toDate));
		
		this.availableStocks = this.getStocks();
		this.portfolio = this.getSavedStocks();
		this.selectedStocks = new ArrayList<Stock>(this.portfolio);
		
		this.fromDateTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				new DatePickerDialog(MainActivity.this, 
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, 
									int monthOfYear, int dayOfMonth) {
								Calendar cal = Calendar.getInstance();
								cal.set(Calendar.YEAR, year);
								cal.set(Calendar.MONTH, monthOfYear);
								cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								
								fromDateTxt.setText(sdf.format(cal.getTime()));
								fromDate = cal.getTime();							
							}
						}, 
						cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		this.toDateTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				new DatePickerDialog(MainActivity.this,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Calendar cal = Calendar.getInstance();
								cal.set(Calendar.YEAR, year);
								cal.set(Calendar.MONTH, monthOfYear);
								cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

								if(cal.getTime().before(fromDate)) {
									Toast.makeText(MainActivity.this, 
											R.string.err_date_to, 
											Toast.LENGTH_LONG).show();
								} else {
									toDateTxt.setText(sdf.format(cal.getTime()));
									toDate = cal.getTime();
								}
							}
						},
						cal.get(Calendar.YEAR), 
						cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

				
		this.stocksBtn.setOnClickListener(new StockSelectAlert(
				availableStocks,
				selectedStocks, this, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int which, long id) {
				CheckedTextView checkedTextView = (CheckedTextView) view;
				if(checkedTextView.isChecked())
					selectedStocks.add(availableStocks.get(which));
				else
					selectedStocks.remove(availableStocks.get(which));
			}
		}, null));
		
		this.savedStocksBtn.setOnClickListener(new StockSelectAlert(
				availableStocks,
				portfolio, this, 
				new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int which, long id) {
				CheckedTextView checkedTextView = (CheckedTextView) view;
				if(checkedTextView.isChecked())
					portfolio.add(availableStocks.get(which));
				else
					portfolio.remove(availableStocks.get(which));
			}
		}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Toast.makeText(MainActivity.this, new PreferencesService().send(portfolio), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Log.e(MainActivity.this.getClass().getName(), e.getLocalizedMessage());
				}		
			}
		}));
		
		this.showBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectedStocks.isEmpty()) {
					Toast.makeText(MainActivity.this, R.string.err_no_stocks, Toast.LENGTH_LONG).show();
					return;
				}
				Map<Stock, List<Price>> prices =  MainActivity.this.getStockPrizes();
				if(prices.isEmpty())
					Toast.makeText(MainActivity.this, "Keine Aktien gefunden", Toast.LENGTH_LONG).show();
				else {
					StockChart chart = new StockChart(prices);
					Intent chartIntent = chart.execute(MainActivity.this);
					startActivity(chartIntent);
				}
			}
		});
	}

	private List<Stock> getSavedStocks() {
		List<Stock> stocks = null;
		try {
			stocks = new PreferencesService().retrieve();
		} catch (Exception e) {
			Log.e(MainActivity.class.getName(), e.getLocalizedMessage());
		}
		return stocks;
	}

	private List<Stock> getStocks() {		
		List<Stock> stocks = null;
		try {
			stocks = new OptionsService().retrieve();
		} catch (Exception e) {
			Log.e(MainActivity.class.getName(), e.getLocalizedMessage());
		}
		return stocks;
	}
	
	private Map<Stock, List<Price>> getStockPrizes() {
		Map<Stock, List<Price>> result = new HashMap<Stock, List<Price>>();
		try {
			PriceService priceService = new PriceService();
			for(Stock st : this.selectedStocks) {
				result.put(st, priceService.retrieve(st, this.fromDate, this.toDate));
			}
		} catch (Exception e) {
			Log.e(MainActivity.class.getName(), e.getLocalizedMessage());
		}
		return result;		
	}
}