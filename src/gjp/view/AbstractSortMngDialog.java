package gjp.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import gjp.domain.Sort;
import gjp.tools.GUITools;
import gjp.tools.ListTableModel;
/**
 * 
 * @author 
 * 弹出分类管理对话框
 */
public abstract class AbstractSortMngDialog extends JDialog {
	//在对话框，显示数据的表格（放分类数据的表格），权限设置是为子类使用
	protected JTable sortDataTable = new JTable();//账户数据列表
	
	
	private JButton closeBtn = new JButton("关闭");
	private JButton addBtn = new JButton("添加");
	private JButton editBtn = new JButton("编辑");
	private JButton delBtn = new JButton("删除");
	
	//
	public AbstractSortMngDialog(JFrame frame) {
		//true 模态化窗口
		super(frame, true);
		this.initDialog();
	}
	
	//protected专门给子类和本类使用
	protected void initDialog() {
		this.init();
		this.addComponent();
		this.addListener();
	}
	
	//设置窗体
	private void init() {
		this.setResizable(false);// 设置窗体大小不可变
		this.setTitle("分类管理：");// 设置标题
		this.setSize(680, 400);// 设置大小
		GUITools.center(this);//设置居中
		this.setLayout(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// 设置关闭按钮
	}
	
	//添加主键
	private void addComponent() {
		// 设置标签标题
		JLabel titleLable = new JLabel();
		titleLable.setFont(new Font("微软雅黑", Font.BOLD ,18));
		titleLable.setText("分类管理");
		titleLable.setBounds(280, 20, 165, 20);//定位
		this.add(titleLable); //添加到对话框

		// 滚动面板,带有滚动条的容器，就是没有数据的黑框
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 100, 620, 160);
		
		sortDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单选（多行数据，鼠标只能选一行）
		sortDataTable.getTableHeader().setReorderingAllowed(false);//列不能移动，列的大小不能改变

		//在带有滚动条的容器上，设置显示表格
		scrollPane.setViewportView(sortDataTable);
		this.add(scrollPane); //在分类对话框上添加滚动面板
		
		// 按钮
		addBtn.setBounds(30, 290, 140, 28);
		this.add(addBtn);
		editBtn.setBounds(270, 290, 140, 28);
		this.add(editBtn);
		delBtn.setBounds(510, 290, 140, 28);
		this.add(delBtn);

		// 关闭按钮
		closeBtn.setBounds(570, 330, 80, 28);
		this.add(closeBtn);
	}

	/**
	 * 显示分类表格，为了在表格中放数据
	 * setTableModel：设置表格Model
	 * List<Sort> 封装的是domain.sort类
	 */
	protected void setTableModel(List<Sort> sortList) {
		String[] colNames = new String[] {"ID", "分类名称", "父分类", "说明"};
		String[] propNames = new String[] {"sid", "sname", "parent", "sdesc"};
		if(sortList == null || sortList.size() == 0) {
			sortDataTable.setModel(new DefaultTableModel(new Object[][] {
					{null, null, null, null}, {null, null, null, null},
					{null, null, null, null}, {null, null, null, null},
					{null, null, null, null}, {null, null, null, null},
					{null, null, null, null}, {null, null, null, null}
				},colNames));
			sortDataTable.setEnabled(false);
			return;
		}
		try {
			sortDataTable.setModel(new ListTableModel<Sort>(sortList, Sort.class, colNames, propNames));
			sortDataTable.setEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("all")//压制警告，不让警告显示
	/*
	 * 编辑，删除分类
	 * 传递选择的第几行
	 * 将选择的那一行，所有数据封装成一个Sort对象
	 */
	protected Sort getSortByTableRow(int row) {
		return ((ListTableModel<Sort>)sortDataTable.getModel()).getInstance(row);
	}

	/**
	 * 
	 * 给组件添加监听器
	 */
	private void addListener() {
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AbstractSortMngDialog.this.dispose();
			}
		});
		/*
		 * 添加分类按钮的事件监听器
		 */
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addSort();
			}
		});
		/*
		 * 编辑分类按钮的事件监听器
		 */
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				editSort();
			}
		});
		/*
		 * 删除分类按钮的事件监听器
		 */
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				deleteSort();
			}
		});
		/*
		 * 显示数据的表格，设置鼠标的双击事件
		 * 调用编辑分类方法
		 */
		sortDataTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == 1) { //1左键，2滚轮，3右键
					if(e.getClickCount() == 2) {
						editSort();		
					}
				}
			}
		});
	}
	/*
	 * 点击添加分类后调用
	 * 子类重写，运行的是子类重写
	 */
	public abstract void addSort();
	/*
	 * 点击编辑分类后调用
	 */
	public abstract void editSort();
	/*
	 * 点击删除分类后调用
	 */
	public abstract void deleteSort();
}
