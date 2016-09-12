package at.jku.stockticker.ui;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import at.jku.stockticker.R;
import at.jku.stockticker.pojo.Stock;

public class StockSelectAlert implements View.OnClickListener {
	
	private Context context;
	private OnItemClickListener onItemClickListener;
	private OnClickListener okListener;
	
	private List<Stock> selected;
	private List<Stock> items;
	
	
	public StockSelectAlert(List<Stock> items, List<Stock> selected, Context context,
			OnItemClickListener onItemClickListener, OnClickListener okListener) {
		super();
		this.items = items;
		this.selected = selected;
		this.context = context;
		this.okListener = okListener;
		this.onItemClickListener = onItemClickListener;
	}


	@Override
	public void onClick(View v) {
		ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(
				context,
				android.R.layout.simple_list_item_multiple_choice,
				items);
		
		ListView listView = new ListView(context);		
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        
		for(int i=0; i<items.size(); i++) {
			if(selected.contains(items.get(i))) {
				listView.setItemChecked(i, true);
			}
		}
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setView(listView);
		dialogBuilder.setNegativeButton(R.string.cancel, null);
		dialogBuilder.setPositiveButton(R.string.ok, okListener);
		dialogBuilder.create().show();
	}
}
