package gjp.controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gjp.domain.Sort;
import gjp.services.SortService;
import gjp.view.AbstractSortMngDialog;

/*
 * 分类管理对话框的控制器
 * 实现分类管理功能
 * 显示分类管理界面，就new这个类
 */
public class SortMngController extends AbstractSortMngDialog {
	//成员位置，创建出service层sortService对象
	private SortService sortService = new SortService();

	public SortMngController(JFrame frame) {
		super(frame);
		//向表格中，填充分类数据，在构造方法中实现
		//父类AbstractSortMngDialog.setTableModel方法，传递List<Sort>集合，向表格中填充数据
		/*
		 * 实现步骤：
		 * 1.调用serives层中方法，获取出List集合
		 * 2.services层调用dao层，获取出List集合
		 * 3.dao层，查询数据库，数据表中的结果集变成List集合，返回
		 * 4.SortMngController中调用父类AbstractSortMngDialog.setTableModel方法，传递List集合
		 * 5.抽取方法调用
		 */
		refresh();
	}

	/*
	 * 添加分类的按钮，点击后调用的方法; 
	 * 开启添加分类的对话框
	 */
	@Override
	public void addSort() {
		new AddSortController(this).setVisible(true);
		refresh();
	}

	/*
	 * 编辑分类的按钮，点击后调用的方法 
	 * 开启编辑分类的对话框
	 * 
	 * 对用户选择的分类数据进行控制
	 * 不选择，选择空行
	 * 父类sortDataTable表格中的getSlecetRow()方法获取选择的行，返回-1，没有选择
	 * 选择的是空行，父类getSortByTableRow()方法，传递选中的行的sort对象
	 * 如果用户选择有效行，把封装好的对象传递给EditSortController对话框，后面的实现和新增几乎一样
	 */
	@Override
	public void editSort() {
		//获取用户选中的行号
		int row = sortDataTable.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "请选择数据");
			return;
		}
		//getSortByTableRow
		Sort sort = getSortByTableRow(row);
		if (sort == null) {
			JOptionPane.showMessageDialog(this, "选择的是空行");
			return;
		}

		//把sort对象传递到编辑分类的对话框中，就要在EditSortController中的构造函数传参
		new EditSortController(this, sort).setVisible(true);
		refresh();
	}

	/*
	 * 点击删除按钮，调用方法
	 * 实现步骤：
	 * 	获取选择的哪一行，判断行是否空
	 * 	正确行，提示对话框，询问用户，是否真的要删除吗
	 * 	确定，调用services层方法delete方法，传递sort对象
	 * 	刷新数据
	 */
	@Override
	public void deleteSort() {
		//获取行号
		int row = this.sortDataTable.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "请选择数据");
			return;
		}
		//getSortByTableRow
		Sort sort = getSortByTableRow(row);
		if (sort == null) {
			JOptionPane.showMessageDialog(this, "选择的是空行");
			return;
		}
		//提示是否删除
		int i = JOptionPane.showConfirmDialog(this, "真的要删除吗", "删除提示", JOptionPane.YES_NO_OPTION);
		//i=0是，i=1否
		if(i == JOptionPane.OK_OPTION) {
			//调用services层方法deleteSort
			sortService.deleteSort(sort);
			JOptionPane.showMessageDialog(this, "删除成功");
			refresh();
		}
	}

	//刷新显示分类数据
	private void refresh() {
		List<Sort> list = sortService.querySortAll();
		super.setTableModel(list);
	}
}
