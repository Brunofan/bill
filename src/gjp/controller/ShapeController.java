package gjp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;

import gjp.services.LedgerServices;
import gjp.tools.DateUtils;
import gjp.tools.JFreeChartUtils;
import gjp.view.AbstractShapeDialog;

public class ShapeController extends AbstractShapeDialog {
	private LedgerServices ledgerServices = new LedgerServices();

	public ShapeController(JDialog dialog) {
		super(dialog);
		initDialog();
	}

	/*
	 * 获取生成的图片路径
	 * 把路径放到集合中
	 * JFreeChartUtils.pie()生成图片
	 */
	@Override
	public List<String> getImagePaths() {
		List<String> listpath = new ArrayList<String>();
		//调用services层方法，获取总金额
		Double moneyPay = ledgerServices.queryTotalMoneyByParent("支出");
		Map<String, Double> map = ledgerServices.querySumMoneyBySort("支出");
		//调用JFreeChartUtils工具类中方法pie生成图片
		String title = "支出 占比图(" + moneyPay + ")(2016年)";
		JFreeChartUtils.pie(title, map, moneyPay, "pay.jpg");
		listpath.add("pay.jpg");
		//调用services层方法，获取总金额
		Double moneyIn = ledgerServices.queryTotalMoneyByParent("收入");
		Map<String, Double> map2 = ledgerServices.querySumMoneyBySort("收入");
		//调用JFreeChartUtils工具类中方法pie生成图片
		String title2 = "收入 占比图(" + moneyIn + ")(2016年)";
		JFreeChartUtils.pie(title2, map2, moneyIn, "in.jpg");
		listpath.add("in.jpg");
		return listpath;
	}

}
