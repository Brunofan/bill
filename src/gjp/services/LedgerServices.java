package gjp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gjp.dao.LedgerDao;
import gjp.dao.SortDao;
import gjp.domain.Ledger;
import gjp.domain.QueryForm;

public class LedgerServices {
	private LedgerDao ledgerDao = new LedgerDao();
	private SortDao sortDao = new SortDao();

	/*
	 * 调用dao层方法getTotalMoney
	 */
	public Double queryTotalMoneyByParent(String parent) {
		return ledgerDao.getTotalMoney(parent);
	}

	/*
	 * 调用dao层querySumMoneyBySort，传递父分类，为了获取这个分类下的求和数据
	 * 按照每个分类的名称进行分组
	 * Map: 键：分类名称  值：这个名称所有金额的总和
	 */
	public Map<String, Double> querySumMoneyBySort(String parent) {
		List<Object[]> list = ledgerDao.querySumMoneyBySort(parent);
		//创建Map集合
		Map<String, Double> map = new HashMap<>();
		for (Object[] l : list) {
			//根据sid值找sname
			Double money = (Double) l[0];
			int sid = (int) l[1];
			String sname = sortDao.getSnameBySid(sid);
			//求和的值和名称存储到map
			map.put(sname, money);
		}
		return map;
	}

	/*
	 * 定义方法，删除账务
	 * 传递lid值
	 */
	public void deleteLedger(int lid) {
		ledgerDao.deleteLedger(lid);
	}

	/*
	 * 定义方法，编辑账务
	 * 传递Ledger对象
	 */
	public void editLedger(Ledger ledger) {
		ledgerDao.editLedger(ledger);
	}

	/*
	 * 定义方法，添加账务
	 * 传递Ledger对象
	 */
	public void addLedger(Ledger ledger) {
		ledgerDao.addLedger(ledger);
	}

	/*
	 * 定义方法，调用sortDao方法getSidBySname，传递sname，获取sid
	 */
	public int getSidBySname(String sname) {
		return sortDao.getSidBySname(sname);
	}

	/*
	 * 定义方法，返回值是Map集合
	 * 作用：根据用户的条件，查询数据库（List集合）
	 * 遍历List集合，统计收入和支出的求和计算
	 * 存储到Map集合
	 * 
	 * 调用dao层方法，查询结果的List集合
	 */
	public Map<String, Object> queryLedgerByQueryForm(QueryForm form) {
		List<Ledger> list = ledgerDao.queryLedgerByQueryForm(form);
		double in = 0.0;
		double pay = 0.0;
		for (Ledger ledger : list) {
			//调用dao层Sort.getSnameBySid()方法，传递sid，获取sname
			int sid = ledger.getSid();
			String sname = sortDao.getSnameBySid(sid);
			ledger.setSname(sname);
			if ("收入".equals(ledger.getParent())) {
				in += ledger.getMoney();
			} else {
				pay += ledger.getMoney();
			}
		}
		//创建Map集合，将数据，list，in，pay存储到Map集合中
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("ledgers", list);
		data.put("in", in);
		data.put("pay", pay);
		//System.out.println(data);
		return data;
	}

}
