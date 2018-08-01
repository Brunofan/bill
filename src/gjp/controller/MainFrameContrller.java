package gjp.controller;

import gjp.view.AbstractMainFrame;

public class MainFrameContrller extends AbstractMainFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * 重写主窗体类的抽象方法 
	 * 打开账务管理对话框
	 */
	@Override
	public void ledgerMng() {
		new LedgerMngController(this).setVisible(true);
	}

	/**
	 * 重写主窗体类的抽象方法 
	 * 打开分类管理对话框
	 */
	@Override
	public void sortMng() {
		//创建分类对话框的子类对象
		new SortMngController(this).setVisible(true);
	}

}
