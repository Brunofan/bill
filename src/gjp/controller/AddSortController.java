package gjp.controller;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import gjp.domain.Sort;
import gjp.services.SortService;
import gjp.view.AbstractOperationSortDialog;

/*
 * 添加分类对话框的控制器
 */
public class AddSortController extends AbstractOperationSortDialog {

	public AddSortController(JDialog dialog) {
		super(dialog);
		//设置标签标题
		titleLabel.setText("添加分类");
		//设置窗体的标题
		super.setTitle("添加分类");
	}

	/*
	 * 添加分类的确定按钮
	 * 实现步骤：
	 * 1.数据验证
	 * 		分类选项
	 * 		分类名称
	 * 	如果数据不符合要求，提示对话框，重新输入
	 * 2.将获取到的数据，封装成Sort对象，sid成员，不需要设置值
	 * 3.将Sort对象传递给services层出来
	 * 4.services层获取Sort对象后，对象传递给dao层
	 * 5.dao层中，将Sort对象中的数据写入到数据表中
	 * 6.提示用户添加成功
	 * 7.重新加载分类功能---在SortMngController做
	 */
	@Override
	public void confirm() {
		//对添加功能的数据，进行验证
		//获取分类下拉菜单，用户选择的值
		//getSelectedItem获取下拉菜单中的选择的内容
		String parent = super.parentBox.getSelectedItem().toString();
		//获取分类名称
		String sname = super.snameTxt.getText().trim();
		//获取分类的描述
		String sddesc = super.sdescArea.getText();
		//验证下拉菜单，只要内容不等于 =请选择= 就可以
		if ("=请选择=".equals(parent)) {
			//弹出对话框，选择错误，必须选择一个
			JOptionPane.showMessageDialog(this, "请选择分类");
			//JOptionPane.showMessageDialog(this, "请选择分类", "错误提示", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//验证分类名称,不是空即可
		if (sname == null || "".equals(sname)) {
			JOptionPane.showMessageDialog(this, "请填写分类名称");
			return;
		}

		//获取到的数据封装成Sort类对象
		Sort sort = new Sort(0, sname, parent, sddesc);
		/*Sort sort = new Sort();
		sort.setParent(parent);
		sort.setSdesc(sddesc);
		sort.setSname(sname);*/

		//调用service层的SortService的addSort()方法传递Sort对象
		//创建service层的SortService的对象
		SortService sortService = new SortService();
		//调用方法addSort传递封装好的Sort对象
		sortService.addSort(sort);
		//new SortService().addSort(sort);
		
		//提示添加分类成功
		JOptionPane.showMessageDialog(this, "添加分类成功", "操作成功", JOptionPane.PLAIN_MESSAGE);
		
		//关闭自己的对话框
		this.dispose();
		
		//重新加载，调用service层的SortService的querySortAll()
		//sortService.querySortAll();
	}

}
