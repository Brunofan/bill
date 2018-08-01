package gjp.controller;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import gjp.domain.Sort;
import gjp.services.SortService;
import gjp.view.AbstractOperationSortDialog;

/*
 * 编辑分类对话框的控制器
 */
public class EditSortController extends AbstractOperationSortDialog {
	private Sort sort;
	public EditSortController(JDialog dialog,Sort sort) {
		super(dialog);
		//设置标签标题
		titleLabel.setText("编辑分类");
		//设置窗体的标题
		super.setTitle("编辑分类");
		this.sort = sort;
		//获取sort对象中的数据，填充到对话框中
		//sort对象中封装的分类填充到下拉菜单
		//setSelectedItem,将菜单中的已有的项目，作为默认项目出现
		super.parentBox.setSelectedItem(sort.getParent());
		super.snameTxt.setText(sort.getSname());
		super.sdescArea.setText(sort.getSdesc());
	}

	/*
	 * 点击编辑界面中的确认按钮，调用的方法
	 * 
	 */
	@Override
	public void confirm() {
		//获取分类下拉菜单内容
		String parent = super.parentBox.getSelectedItem().toString();
		//获取分类名称
		String sname = super.snameTxt.getText().trim();
		//分类描述
		String sdesc = super.sdescArea.getText();
		
		//此处数据是否合法，和添加分类的验证方式相同
		if("=请选择=".equals(parent)) {
			JOptionPane.showMessageDialog(this, "请选择分类");
			return;
		}
		if(sname==null || sname.isEmpty()) {
			JOptionPane.showMessageDialog(this, "请输入分类名称");
			return;
		}
		
		//获取到的数据封装成Sort对象
		sort.setParent(parent);
		sort.setSdesc(sdesc);
		sort.setSname(sname);
		//调用services层内容，传递sort对象，进行修改
		SortService sortService = new SortService();
		sortService.editSort(sort);
		//关闭自己
		this.dispose();
		JOptionPane.showMessageDialog(this, "编辑成功");
	}

	

	}


