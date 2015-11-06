package com.dangdang;

import java.awt.Font;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dangdang.client.DBAction;
import com.dangdang.data.FuncQuery;
import com.dangdang.util.Config;
import com.dangdang.verifier.analysis.CTR_SaleWeekAnalysis;

public class CTR_AnalysisScheduler {
	private static Logger logger = Logger.getLogger(CTR_AnalysisScheduler.class);
	public static List<Map<Integer,Double>> swList = new ArrayList<Map<Integer,Double>>();
	public static List<Map<Integer,Double>> swaList = new ArrayList<Map<Integer,Double>>();
	public static Config confg = new Config();

	{
		PropertyConfigurator.configure("conf/ctr_log4j.properties");
	}
	
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void clearup() {
	double sw_ctr5 =0.0;
	double sw_ctr10 =0.0;
	double sw_ctr30 =0.0;
	double sw_ctr60 =0.0;
	double swa_ctr5 =0.0;
	double swa_ctr10 =0.0;
	double swa_ctr30 =0.0;
	double swa_ctr60 =0.0;
	int iCount=0;
	for(Map<Integer,Double> swMap:swList){
		for(Map.Entry<Integer, Double> entry:swMap.entrySet()){
			iCount++;
			switch(entry.getKey()){
			case 0:
				sw_ctr5 =sw_ctr5+ entry.getValue()/5;
			case 1:
				sw_ctr10 =sw_ctr10+ entry.getValue()/10;
			case 2:
				sw_ctr30 =sw_ctr30+ entry.getValue()/30;
			case 3:
				sw_ctr60 =sw_ctr60+ entry.getValue()/60;
			}
		}
	}
	
	for(Map<Integer,Double> swaMap:swaList){
		for(Map.Entry<Integer, Double> entry:swaMap.entrySet()){
			switch(entry.getKey()){
			case 0:
				swa_ctr5 =swa_ctr5+ entry.getValue()/5;
			case 1:
				swa_ctr10 =swa_ctr10+ entry.getValue()/10;
			case 2:
				swa_ctr30 =swa_ctr30+ entry.getValue()/30;
			case 3:
				swa_ctr60 =swa_ctr60+ entry.getValue()/60;
			}
		}
	}
	
	logger.fatal("********************"+confg.get_baseURL()+"***********************");
	logger.fatal("****************Count:"+iCount+"**********************************");
	logger.fatal("*	前5个品平均销量："+sw_ctr5+"	前5个品平均销售额:"+swa_ctr5+"	*");
	logger.fatal("*	前10个品平均销量："+sw_ctr10+"	前10个品平均销售额:"+swa_ctr10+"	*");
	logger.fatal("*	前30个品平均销量："+sw_ctr30+"	前30个品平均销售额:"+swa_ctr30+"	*");
	logger.fatal("*	前60个品平均销量："+sw_ctr60+"	前60个品平均销售额:"+swa_ctr60+"	*");
	logger.fatal("*************************************************************");
	
	try {
		
		String flag = "8888";
		if(confg.get_baseURL().contains(flag)){
			File tmp_result = new File("temp_8888.txt");
			FileWriter writer = new FileWriter(tmp_result);
			writer.append("8888_前5个品平均销量="+sw_ctr5+"\n");
			writer.append("8888_前5个品平均销售额="+swa_ctr5+"\n");
			writer.append("8888_前10个品平均销量="+sw_ctr10+"\n");
			writer.append("8888_前10个品平均销售额="+swa_ctr10+"\n");
			writer.append("8888_前30个品平均销量="+sw_ctr30+"\n");
			writer.append("8888_前30个品平均销售额="+swa_ctr30+"\n");
			writer.append("8888_前60个品平均销量="+sw_ctr60+"\n");
			writer.append("8888_前60个品平均销售额="+swa_ctr60+"\n");
			writer.flush();
			writer.close();
		}else{
			File tmp_result = new File("temp_9999.txt");
			FileWriter writer = new FileWriter(tmp_result);
			writer.append("9999_前5个品平均销量="+sw_ctr5+"\n");
			writer.append("9999_前5个品平均销售额="+swa_ctr5+"\n");
			writer.append("9999_前10个品平均销量="+sw_ctr10+"\n");
			writer.append("9999_前10个品平均销售额="+swa_ctr10+"\n");
			writer.append("9999_前30个品平均销量="+sw_ctr30+"\n");
			writer.append("9999_前30个品平均销售额="+swa_ctr30+"\n");
			writer.append("9999_前60个品平均销量="+sw_ctr60+"\n");
			writer.append("9999_前60个品平均销售额="+swa_ctr60+"\n");
			writer.flush();
			writer.close();
		}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	}

	// CTR分析
	@Test(enabled = false)
	public void CTRSaleWeekAnalysis_0() {
		try {
		DBAction action = new DBAction();
		action.setFuncCondition("id MOD 4 =0");
		List<FuncQuery> list = action.getFuncQuery();
		for(FuncQuery query: list){
			List<Map<Integer,Double>> resultList = CTR_SaleWeekAnalysis.verify(query);
			if(resultList==null){
				continue;
			}
			if(resultList.size()==2){
				swList.add(resultList.get(0));
				swaList.add(resultList.get(1));
			}
		}
			
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}
	// CTR分析
	@Test(enabled = false)
	public void CTRSaleWeekAnalysis_1() {
		try {
		DBAction action = new DBAction();
		action.setFuncCondition("id MOD 4 =1");
		List<FuncQuery> list = action.getFuncQuery();
		for(FuncQuery query: list){
			List<Map<Integer,Double>> resultList = CTR_SaleWeekAnalysis.verify(query);
			if(resultList.size()==2){
				swList.add(resultList.get(0));
				swaList.add(resultList.get(1));
			}
		}
			
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}
	// CTR分析
	@Test(enabled = false)
	public void CTRSaleWeekAnalysis_2() {
		try {
		DBAction action = new DBAction();
		action.setFuncCondition("id MOD 4 =2");
		List<FuncQuery> list = action.getFuncQuery();
		for(FuncQuery query: list){
			List<Map<Integer,Double>> resultList = CTR_SaleWeekAnalysis.verify(query);
			if(resultList.size()==2){
				swList.add(resultList.get(0));
				swaList.add(resultList.get(1));
			}
		}
			
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}
	// CTR分析
	@Test(enabled = false)
	public void CTRSaleWeekAnalysis_3() {
		try {
		DBAction action = new DBAction();
		action.setFuncCondition("id MOD 4 =3");
		List<FuncQuery> list = action.getFuncQuery();
		for(FuncQuery query: list){
			List<Map<Integer,Double>> resultList = CTR_SaleWeekAnalysis.verify(query);
			if(resultList.size()==2){
				swList.add(resultList.get(0));
				swaList.add(resultList.get(1));
			}
		}
			
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String exception = baos.toString();
			logger.error(" - [LOG_EXCEPTION] - " + exception);
			e.printStackTrace();
		} finally {
		}
	}

	public static JFreeChart createPieChart(DefaultPieDataset ds, String title) {

		StandardChartTheme mChartTheme = new StandardChartTheme("CN");
		mChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 20));
		// 设置轴向字体
		mChartTheme.setLargeFont(new Font("宋体", Font.CENTER_BASELINE, 15));
		// 设置图例字体
		mChartTheme.setRegularFont(new Font("宋体", Font.CENTER_BASELINE, 15));
		ChartFactory.setChartTheme(mChartTheme);
		// 通过工程创建3D饼图
		JFreeChart pieChart = ChartFactory.createPieChart3D(title, ds, true, true, false);
		pieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// 得到3D饼图的plot对象
		PiePlot3D piePlot = (PiePlot3D) pieChart.getPlot();
		// 设置旋转角度
		piePlot.setStartAngle(290);
		// 设置旋转方向
		piePlot.setDirection(Rotation.CLOCKWISE);
		// 设置透明度
		piePlot.setForegroundAlpha(0.5f);
		piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		piePlot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));
		return pieChart;

	}

}
