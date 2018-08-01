package gjp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import gjp.domain.Ledger;
import gjp.domain.QueryForm;
import gjp.tools.DateUtils;
import gjp.tools.JDBCUtils;

public class LedgerDao {

	private SortDao sortDao = new SortDao();
	private QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

	/*
	 * 定义方法，查询今年所有数据的总和
	 */
	public Double getTotalMoney(String parent){
		String sql = "select sum(money),sid from gjp_ledger where parent = ? "
				+ "and createtime like ?";
		try {
			Object[] params = {parent, "2016%"};
			return (Double) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 定义方法，通过分类名称查询
	 */
	public List<Object[]> querySumMoneyBySort(String parent){
		String sql = "select sum(money),sid from gjp_ledger where parent = ? "
				+ "and createtime like ? group by sid";
		try {
			Object[] params = {parent,"2016%"};
			return qr.query(sql, new ArrayListHandler(), params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public void deleteLedger(int lid) {
		try {
			qr.update("delete from gjp_ledger where lid = ?", lid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/*
	 * 定义方法，编辑账务
	 * 参数Ledger对象
	 */
	public void editLedger(Ledger ledger) {
		String sql = "update gjp_ledger set parent = ?, money = ?, sid = ?,"
				+ " account = ? ,createtime = ?,ldesc = ? where lid=?";
		try {
		Object[] params = { ledger.getParent(), ledger.getMoney(), ledger.getSid(), ledger.getAccount(),
				ledger.getCreatetime(), ledger.getLdesc() ,ledger.getLid()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义方法，添加账务
	 * 参数Ledger对象
	 */
	public void addLedger(Ledger ledger) {
		String sql = "insert into gjp_ledger (parent,money,sid,account,createtime,ldesc) values(?,?,?,?,?,?)";
		try {
			Object[] params = { ledger.getParent(), ledger.getMoney(), ledger.getSid(), ledger.getAccount(),
					ledger.getCreatetime(), ledger.getLdesc() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 定义方法，实现组合查询功能
	 * 传递QueryForm对象
	 * 返回List<Ledger>集合
	 * 根据form中的数据，进行sql语句的编写
	 */
	public List<Ledger> queryLedgerByQueryForm(QueryForm form) {
		//查询语句中，？也是不确定因素，参数用容器进行存储
		List<String> params = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		//查询条件，只有日期
		builder.append("select * from gjp_ledger where createtime between ? and ?");
		params.add(form.getBegin());
		params.add(form.getEnd());

		//查询条件加上收入或支出
		if ("收入".equals(form.getParent()) || "支出".equals(form.getParent())) {
			builder.append(" and parent = ?");
			params.add(form.getParent());
		}

		//查询条件再加分类名称
		if (!"-请选择-".equals(form.getSname())) {
			//获取sid
			int sid = sortDao.getSidBySname(form.getSname());
			builder.append(" and sid = ?");
			params.add(sid + "");
		}
		try {
			List<Ledger> list = qr.query(builder.toString(), new BeanListHandler<Ledger>(Ledger.class),
					params.toArray());
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

/*public static void main(String[] args) {
	LedgerDao dao = new LedgerDao();
	System.out.println(dao.getTotalMoney("支出"));
	System.out.println(dao.querySumMoneyBySort("支出"));
}*/
}
