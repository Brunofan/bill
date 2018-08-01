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
public class EditLedgerController extends AbstractOperationLedgerDialog {

	private SortService sortService = new SortService();
	private Ledger ledger;
	private LedgerServices ledgerServices = new LedgerServices();
	
	public EditLedgerController(JDialog dialog, Ledger ledger) {
		super(dialog);
		titleLabel.setText("编辑账务");
		super.setTitle("编辑账务");
		this.ledger = ledger;
		parentBox.setSelectedItem(ledger.getParent());
		//setSelectedItem，将菜单原有中的内容选择一个设置成默认选项
		//sortBox.setSelectedItem(ledger.getSname());
		//获取到分类的名称，转字符串数组
		String[] items = { ledger.getSname() };
		//调用的是菜单的方法，重新设置菜单 数据
		sortBox.setModel(new DefaultComboBoxModel<>(items));
		accountTxt.setText(ledger.getAccount());
		moneyTxt.setText(ledger.getMoney() + "");
		createtimeTxt.setText(ledger.getCreatetime());
		ldescTxt.setText(ledger.getLdesc());
	}

	@Override
	public void changeParent() {
		//获取收支的选项
		String parent = super.parentBox.getSelectedItem().toString();
		//情况一
		if ("-请选择-".equals(parent)) {
			sortBox.setModel(new DefaultComboBoxModel(new String[] { "-请选择-" }));
		}

		//情况二，查询分类的具体内容
		if ("收入".equals(parent) || "支出".equals(parent)) {
			//调用services层方法querySortNameByParent(parent)查询所有分类名称
			//获取一个List.toArray()集合，集合中的数据，填充到下拉菜单中
			List<Object> list = sortService.querySortNameByParent(parent);
			list.add(0, "-请选择-");
			sortBox.setModel(new DefaultComboBoxModel(list.toArray()));
		}
	}

	/*
	 * 点击编辑的确定按钮，调用方法
	 * 获取用户选择的数据
	 * 对数据进行验证
	 * 数据封装成Ledger对象
	 * 调用service层方法
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

		if ("-请选择-".equals(parent)) {
			JOptionPane.showMessageDialog(this, "请选择收支");
			return;
		}

		if ("-请选择-".equals(sname)) {
			JOptionPane.showMessageDialog(this, "请选择分类名称");
			return;
		}

		if (account == null || account.isEmpty()) {
			JOptionPane.showMessageDialog(this, "请填写账户");
			return;
		}

		//获取到的金额，由String转成double
		double money = 0;
		try {
			money = Double.parseDouble(sMoney);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "必须填写数字");
			return;
		}

		if (money <= 0) {
			JOptionPane.showMessageDialog(this, "数字必须大于0");
			return;
		}

		//将数据封装成Ledger对象
		int lid = ledger.getLid();
		ledger = new Ledger();
		ledger.setAccount(account);
		ledger.setCreatetime(createtime);
		ledger.setLdesc(ldesc);
		ledger.setMoney(money);
		ledger.setParent(parent);
		ledger.setLid(lid);
		int sid = ledgerServices.getSidBySname(sname);
		ledger.setSid(sid);
		
		//调用service层方法editLedger
		ledgerServices.editLedger(ledger);
		this.dispose();
		JOptionPane.showMessageDialog(this, "编辑成功");
	}

}
