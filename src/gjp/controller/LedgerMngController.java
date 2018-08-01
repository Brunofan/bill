package gjp.controller;

import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gjp.domain.Ledger;
import gjp.domain.QueryForm;
import gjp.services.LedgerServices;
import gjp.services.SortService;
import gjp.view.AbstractLedgerMngDialog;

/**
 * 
 * @author Ivan 账务管理对话框的控制层
 */

public class LedgerMngController extends AbstractLedgerMngDialog {
	private SortService sortService = new SortService();
	private LedgerServices ledgerServices = new LedgerServices();
	
	public LedgerMngController(JFrame frame) {
		super(frame);

	}

	/*
	 * 点击添加账务按钮，弹出添加账务对话框
	 */
	@Override
	public void addLedger() {
		new AddLedgerController(this).setVisible(true);
	}

	/*
	 * 点击编辑账务按钮，弹出编辑账务对话框
	 */
	@Override
	public void editLedger() {
		//获取选择的行
		int row = ledgerDataTable.getSelectedRow();
		if(row < 0) {
			JOptionPane.showMessageDialog(this, "请选择要编辑的数据");
			return;
		}
		
		//调用父类方法getLedgerByTableRow，参数行号，获取ledger对象
		Ledger ledger = getLedgerByTableRow(row);
		if(ledger == null) {
			JOptionPane.showMessageDialog(this, "选择的是空行");
			return;
		}
		
		//弹出编辑账务对话框，需要传入ledger对象，
		new EditLedgerController(this,ledger).setVisible(true);
		queryLedger();
	}

	@Override
	public void deleteLedger() {
		int row = ledgerDataTable.getSelectedRow();
		if(row < 0) {
			JOptionPane.showMessageDialog(this, "没有选择数据");
			return;
		}
		
		Ledger ledger = getLedgerByTableRow(row);
		if(ledger == null) {
			JOptionPane.showMessageDialog(this, "选择的是空行");
			return;
		}
		//确认删除的对话框
		int result = JOptionPane.showConfirmDialog(this,"真的要删除吗", "删除提示", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			//调用service层删除方法
			ledgerServices.deleteLedger(ledger.getLid());
			//this.dispose();
			JOptionPane.showMessageDialog(this, "删除成功");
		}
		queryLedger();
	}
	
	/*
	 * 点击查询按钮，实现查询功能
	 * 获取的是services层的查询结果
	 * 结果做成Map集合
	 * 	key1 value:查询数据的List集合
	 * 	key2 value:所有收入的综合
	 * 	key3 value：所有支出的总和
	 * 	map.put方法
	 */
	@Override
	public void queryLedger() {
		//先将查询条件封装
		String begin = super.beginDateTxt.getText();
		String end = super.endDateTxt.getText();
		String parent = super.parentBox.getSelectedItem().toString();
		String sname = super.sortBox.getSelectedItem().toString();
		QueryForm form = new QueryForm(begin, end, parent, sname);
		//调用services层方法，queryLedgerByQueryForm
		Map<String,Object> data = ledgerServices.queryLedgerByQueryForm(form);
		List<Ledger> list = (List<Ledger>) data.get("ledgers");
		double in = (double) data.get("in");
		double pay = (double) data.get("pay");
		//将查询的List填充到 JTable中
		super.setTableModel(list);
		//把求和放到Label中
		super.inMoneyTotalLabel.setText("总收入：" + in + "元");
		super.payMoneyTotalLabel.setText("总支出：" + pay + "元");
	}

	/*
	 * 菜单联动，调用的方法
	 * 父类AbstractLedgerMngDialog中监听的事件parentBox.addActionListener
	 * 情况一：
	 * 	收支：请选择
	 * 	分类：请选择
	 * 情况二：
	 * 	收支：收入/支出
	 * 	分类：所有的支出和收入
	 * 	根据收支选择，去数据库中，查询所有的分类内容
	 * 情况三：
	 * 	收支：收入，或者选择支出
	 * 	分类：对应的
	 * 	根据收支选择，去数据库中，查询所有的分类内容
	 */
	@Override
	public void parentChange() {
		//获取收支的选项
		String parent = super.parentBox.getSelectedItem().toString();
		//情况一
		if("-请选择-".equals(parent)) {
			sortBox.setModel(new DefaultComboBoxModel(new String[] {"-请选择-"}));
		}
		
		//情况二
		if("收入/支出".equals(parent)) {
			//调用services层方法querySortNameAll()查询所有分类名称
			//获取一个List.toArray()集合，集合中的数据，填充到下拉菜单中
			List<Object> list =sortService.querySortNameAll();
			list.add(0,"-请选择-");
			sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
		}
		
		//情况三，查询分类的具体内容
		if("收入".equals(parent) || "支出".equals(parent)) {
			//调用services层方法querySortNameByParent(parent)查询所有分类名称
			//获取一个List.toArray()集合，集合中的数据，填充到下拉菜单中
			List<Object> list =sortService.querySortNameByParent(parent);
			list.add(0,"-请选择-");
			sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
		}
		
		
	}

	@Override
	public void pie() {
		new ShapeController(this).setVisible(true);
	}

}
