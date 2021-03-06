package com.dangdang.verifier.analysis;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class PosiCTR_ResultAnalysisTool {
	
	public static void makeBarChart3D(CategoryDataset ds,String title,String valueLabel) {
//		String title_wsa = "当当网 周销售额柱状图";
		// 获得数据集
			JFreeChart chart = ChartFactory.createBarChart3D(title,
					// 图表标题
							"点击位置",
							// 目录轴的显示标签
							valueLabel,
							// 数值轴的显示标签
							ds,
							// 数据集
							PlotOrientation.VERTICAL,
							// 图表方向：水平、垂直
							true,
							// 是否显示图例
							true,
							// 是否生成工具（提示）
							true
					// 是否生成URL链接
							);
					// 设置标题字体
					Font font = new Font("宋体", Font.BOLD, 18);
					TextTitle textTitle = new TextTitle(title);
					textTitle.setFont(font);
					chart.setTitle(textTitle);
					chart.setTextAntiAlias(false);
					// 设置背景色
					chart.setBackgroundPaint(new Color(255, 255, 255));
					// 设置图例字体
					LegendTitle legend = chart.getLegend(0);
					legend.setItemFont(new Font("宋体", Font.TRUETYPE_FONT, 14));
					// 获得柱状图的Plot对象
					CategoryPlot plot = chart.getCategoryPlot();
					// 取得横轴
					CategoryAxis categoryAxis = plot.getDomainAxis();
					// 设置横轴显示标签的字体
					categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));
					// 设置横轴标记的字体
					categoryAxis.setTickLabelFont(new Font("宋休", Font.TRUETYPE_FONT, 16));
					// 取得纵轴
					NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
					// 设置纵轴显示标签的字体
					numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));
					/**************************************************************/
					ChartFrame frame = new ChartFrame(title, chart, true);
					frame.pack();
					frame.setVisible(true);
					
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static List<CategoryDataset> getDataset() {
		List<CategoryDataset> list = new ArrayList<CategoryDataset>();
		File f1 = new File("D:\\workspace\\functionVerifier\\temp_9999.txt");
		File f2 = new File("D:\\TestResult\\functionVerifier\\temp_8888.txt");
//		private static final String Config_path = "conf/config.properties";
		Properties pro = new Properties();
		InputStream in1;
		InputStream in2;
		double [][]data_sw = new double[2][4];
		double [][]data_swa = new double[2][4];
		try {
			in1 = new FileInputStream(f1);
			InputStreamReader reader1 = new InputStreamReader (in1,"GBK");
			in2 = new FileInputStream(f2);
			InputStreamReader reader2 = new InputStreamReader (in2,"GBK");
			pro.load(reader1);
			pro.load(reader2);
			//double sw_ctr5_8888
			data_sw[0][0] = Double.valueOf(pro.getProperty("8888_前5个品平均销量"));
			data_sw[0][1] = Double.valueOf(pro.getProperty("8888_前10个品平均销量"));
			data_sw[0][2] = Double.valueOf(pro.getProperty("8888_前30个品平均销量"));
			data_sw[0][3] = Double.valueOf(pro.getProperty("8888_前60个品平均销量"));
			
//			double sw_ctr5_9999
			data_sw[1][0] = Double.valueOf(pro.getProperty("9999_前5个品平均销量"));
			data_sw[1][1] = Double.valueOf(pro.getProperty("9999_前10个品平均销量"));
			data_sw[1][2] = Double.valueOf(pro.getProperty("9999_前30个品平均销量"));
			data_sw[1][3] = Double.valueOf(pro.getProperty("9999_前60个品平均销量"));
			
//			double swa_ctr5_8888
			data_swa[0][0]= Double.valueOf(pro.getProperty("8888_前5个品平均销售额"));
			data_swa[0][1] = Double.valueOf(pro.getProperty("8888_前10个品平均销售额"));
			data_swa[0][2] = Double.valueOf(pro.getProperty("8888_前30个品平均销售额"));
			data_swa[0][3] = Double.valueOf(pro.getProperty("8888_前60个品平均销售额"));
			
//			double swa_ctr5_9999
			data_swa[1][0]= Double.valueOf(pro.getProperty("9999_前5个品平均销售额"));
			data_swa[1][1] = Double.valueOf(pro.getProperty("9999_前10个品平均销售额"));
			data_swa[1][2] = Double.valueOf(pro.getProperty("9999_前30个品平均销售额"));
			data_swa[1][3] = Double.valueOf(pro.getProperty("9999_前60个品平均销售额"));
			
			double rate_ws5 = (data_sw[0][0]-data_sw[1][0])/data_sw[0][0]*100;
			double rate_ws10 = (data_sw[0][1]-data_sw[1][1])/data_sw[0][1]*100;
			double rate_ws30 = (data_sw[0][2]-data_sw[1][2])/data_sw[0][2]*100;
			double rate_ws60 = (data_sw[0][3]-data_sw[1][3])/data_sw[0][3]*100;
				
			double rate_wsa5 = (data_swa[0][0]-data_swa[1][0])/data_swa[0][0]*100;
			double rate_wsa10 = (data_swa[0][1]-data_swa[1][1])/data_swa[0][1]*100;
			double rate_wsa30 = (data_swa[0][2]-data_swa[1][2])/data_swa[0][2]*100;
			double rate_wsa60 = (data_swa[0][3]-data_swa[1][3])/data_swa[0][3]*100;
					
					System.out.println("*********************************************************");
					System.out.println("*	前5个品的销售量增长:"+rate_ws5+"%	*");
					System.out.println("*	前10个品的销售量增长:"+rate_ws10+"%	*");
					System.out.println("*	前30个品的销售量增长:"+rate_ws30+"%	*");
					System.out.println("*	前60个品的销售量增长:"+rate_ws60+"%	*");
					System.out.println("*********************************************************");
					System.out.println("*	前5个品的销售额增长:"+rate_wsa5+"%	*");
					System.out.println("*	前10个品的销售额增长:"+rate_wsa10+"%	*");
					System.out.println("*	前30个品的销售额增长:"+rate_wsa30+"%	*");
					System.out.println("*	前60个品的销售额增长:"+rate_wsa60+"%	*");
					System.out.println("*********************************************************");
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
//		double [][]data_sw={{13963.56528874657,14513.91059890104,7484.110249890865,13963.56528874657},{13514.885993438666,14057.56729397579,7256.725004712124,13514.885993438666}};
////		double [] [] data_sw = {{573965.855701966,595948.1016537147,307094.9073542841,208393.05439189522},{552303.635491115,573681.0861376631,296019.637100649,201025.30904672737}};
		String[] rowKeys = { "8888", "9999"};
		String[] columnKeys = { "前5个品", "前10个品", "前30个品", "前60个品"};
		CategoryDataset dataset_sw = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data_sw);
		CategoryDataset dataset_swa = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data_swa);
		list.add(dataset_sw);
		list.add(dataset_swa);
		return list;
	}
	
	
	public static void main(String[] args) {
		List<CategoryDataset> list = getDataset();
		CategoryDataset ds = list.get(0);
		makeBarChart3D(ds,"当当网 周销售量柱状图","平均周销售量");
		CategoryDataset ds1 = list.get(1);
		makeBarChart3D(ds1,"当当网 周销售额柱状图","平均周销售额");
	}
}
