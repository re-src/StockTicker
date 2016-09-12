package at.jku.stockticker.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import at.jku.stockticker.R;
import at.jku.stockticker.pojo.Price;
import at.jku.stockticker.pojo.Stock;

public class StockChart {

	private Map<Stock, List<Price>> data;
	
	public StockChart(Map<Stock, List<Price>> stockPrizes) {
		this.data = stockPrizes;
	}

	public Intent execute(Context context) {
		String[] titles = new String[this.data.size()];	
		int [] colors = new int[this.data.size()]; 
		PointStyle[] styles = new PointStyle[this.data.size()]; 
		
		List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();
		
		Date dateMin = new Date(System.currentTimeMillis());
		Date dateMax = new Date(0);
		double valMin = 10e12;
		double valMax = -1;
		
		int j = 0;
		
		for(Map.Entry<Stock, List<Price>> entry : this.data.entrySet()) {
			List<Price> prizes = entry.getValue();
			double[] val = new double[prizes.size()];
			Date[] dat = new Date[prizes.size()];
			
			for(int i=0; i<prizes.size(); i++) {
				val[i] = prizes.get(i).getPrice();
				dat[i] = prizes.get(i).getTime();
				
				if(dat[i].before(dateMin))
					dateMin = dat[i];
				
				if(dat[i].after(dateMax))
					dateMax = dat[i];
				
				if(val[i] < valMin)
					valMin = val[i];
				
				if(val[i] > valMax)
					valMax = val[i];
			}
			
			titles[j] = ""+entry.getKey().getName();
			colors[j] = Color.rgb(100+(int)(Math.random()*155), 100+(int)(Math.random())*155, 100+(int)(Math.random()*155));
			styles[j] = PointStyle.TRIANGLE;
			values.add(val);
			dates.add(dat);
			j++;
		}
	    
	    
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    renderer.setXLabels(10);
	    renderer.setYLabels(10);
	    renderer.setXLabelsAngle(30);
	    renderer.setFitLegend(true);
	    renderer.setXLabelsPadding(10);
	
	    setChartSettings(
	    		renderer, 
	    		context.getString(R.string.chart_title), 
	    		"",//context.getString(R.string.chart_xlabel), 
	    		context.getString(R.string.chart_ylabel), 
	    		dateMin.getTime(),
	    		dateMax.getTime(),
	    		valMin - (valMax-valMin)/4,
	    		valMax + (valMax-valMin)/4,
	    		Color.LTGRAY, 
	    		Color.LTGRAY);
	    
	    return ChartFactory.getTimeChartIntent(context, this.buildDataset(titles, dates, values), renderer, "dd.MM HH:mm");
	}

	private XYMultipleSeriesDataset buildDataset(String[] titles,
			List<Date[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	private XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}
}
