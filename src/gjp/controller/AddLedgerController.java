package gjp.controller;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import gjp.domain.Ledger;
import gjp.services.LedgerServices;
import gjp.services.SortService;
import gjp.view.AbstractOperationLedgerDialog;
/*
 * 
 */
public class AddLedgerController extends AbstractOperationLedgerDialog {
	
	private SortService sortService = new SortService();
	private LedgerServices ledgerServices = new LedgerServices();
	
	public AddLedgerController(JDialog dialog) {
		super(dialog);
		titleLabel.setText("添加账务");
		super.setTitle("添加账务");
	}

	@Override
	public void changeParent() {
		//获取收支的选项
		String parent = super.parentBox.getSelectedItem().toString();
		//情况一
		if("-请选择-".equals(parent)) {
			sortBox.setModel(new DefaultComboBoxModel(new String[] {"-请选择-"}));
		}
		
		
		//情况二，查询分类的具体内容
		if("收入".equals(parent) || "支出".equals(parent)) {
			//调用services层方法querySortNameByParent(parent)查询所有分类名称
			//获取一个List.toArray()集合，集合中的数据，填充到下拉菜单中
			List<Object> list =sortService.querySortNameByParent(parent);
			list.add(0,"-请选择-");
			sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
		}
	}

	/*
	 * 点击添加按钮实现的功能
	 * 验证用户填写的数据
	 * 	分类，分类名称
	 * 	金额>0，必须是数字
	 * 	帐户必须填写
	 * 用户所有的数据封装成Ledger对象
	 * 传递给service层，进行添加
	 */
	@Override
	public void confirm() {
		//获取添加按钮框中的数据
		String parent = super.parentBox.getSelectedItem().toString();
		String sname = sortBox.getSelectedItem().toString();
		String account = accountTxt.getText();
		String createtime = createtimeTxt.getText();
		String sMoney = moneyTxt.getText();
		String ldesc = ldescTxt.getText();
		
		if("-请选择-".equals(parent)) {
			JOptionPane.showMessageDialog(this, "请选择收支");
			return;
		}
		
		if("-请选择-".equals(sname)) {
			JOptionPane.showMessageDialog(this, "请选择分类名称");
			return;
		}
		
		if(account== null || account.isEmpty()) {
			JOptionPane.showMessageDialog(this, "请填写账户");
			return;
		}
		
		//获取到的金额，由String转成double
		double money = 0;
		try {
			money = Double.parseDouble(sMoney);
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "必须填写数字");
			return;
		}
		
		if(money <= 0) {
			JOptionPane.showMessageDialog(this, "数字必须大于0");
			return;
		}
		
		//将数据封装成Ledger对象
		//第一个0，数据表主键，给假的。 第二个0，sid，是通过sname查询sort表获取的，给假的
		//调用service层方法获取sid
		int sid = ledgerServices.getSidBySname(sname);
		Ledger ledger = new Ledger(0, parent, money, sid, account, createtime, ldesc, sname);
		ledgerServices.addLedger(ledger);
		
		this.dispose();
		JOptionPane.showMessageDialog(this, "添加账务成功");
	}

}
