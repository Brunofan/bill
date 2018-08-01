package gjp.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import gjp.domain.Sort;
import gjp.tools.JDBCUtils;

/*
 * 访问数据库的类
 * 负责分类功能
 */
public class SortDao {
	// 类的成员位置定义QueryRunner对象，所有的方法都可以直接使用
	private QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

	/*
	 * 定义一个方法，传递分类名称，返回分类ID
	 */
	public int getSidBySname(String sname) {
		String sql = "select sid from gjp_sort where sname= ?";
		try {
			return (int)qr.query(sql, new ScalarHandler<Object>(), sname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义一个方法，传递分类id，返回分类名称
	 */
	public String getSnameBySid(int sid) {
		String sql = "select sname from gjp_sort where sid=?";
		try {
			return (String)qr.query(sql, new ScalarHandler<Object>(), sid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 定义方法，查询所有的分类的名称
	 * services层调用
	 */
	public List<Object> querySortNameByName(String parent) {
		String sql = "select sname from gjp_sort where parent = ?";
		try {
			List<Object> query = qr.query(sql, new ColumnListHandler<Object>(), parent);
			//System.out.println(query);
			return query;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义方法，查询所有的分类的名称
	 * services层调用
	 */
	public List<Object> querySortNameAll() {
		String sql = "select sname from gjp_sort";
		try {
			List<Object> query = qr.query(sql, new ColumnListHandler<Object>());
			//System.out.println(query);
			return query;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义方法，删除分类数据
	 * 方法传递参数，sort对象
	 * 由services层调用
	 */
	public void deleteSort(Sort sort) {
		String sql = "delete from gjp_sort where sid=?";
		try {
			Object param = sort.getSid();
			qr.update(sql, param);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义方法，编辑分类数据
	 * 方法传递参数，sort对象
	 * 由services层调用
	 */
	public void editSort(Sort sort) {
		String sql = "update gjp_sort set sname=?,parent=?,sdesc=? where sid = ?";
		try {
			Object[] params = { sort.getSname(), sort.getParent(), sort.getSdesc(), sort.getSid() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义方法，添加分类数据
	 * 方法需要传递参数Sort对象
	 * 添加的及时Sort对象中的数据
	 * 没有返回值
	 * 由services层调用
	 */
	public void addSort(Sort sort) {
		String sql = "insert into gjp_sort(sname,parent,sdesc) values(?,?,?)";
		try {
			//定义sql语句中的参数，Object数组
			Object[] params = { sort.getSname(), sort.getParent(), sort.getSdesc() };
			//QueryRunner方法update
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义一个方法，查询出所有分类数据 返回List集合，
	 * 泛型Sort类型 
	 * querySortAll
	 */
	public List<Sort> querySortAll() {
		// sql
		String sql = "select * from gjp_sort";
		// QueryRunner方法query查询数据表，结果集的处理方式BeanListHandler
		try {
			List<Sort> list = qr.query(sql, new BeanListHandler<Sort>(Sort.class));
			return list;
		} catch (SQLException e) {
			// 手动抛出运行时异常
			throw new RuntimeException(e);
		}

	}
}
