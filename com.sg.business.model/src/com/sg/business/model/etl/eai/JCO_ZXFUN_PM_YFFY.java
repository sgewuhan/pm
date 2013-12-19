package com.sg.business.model.etl.eai;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.commons.util.Utils;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.FieldIterator;
import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.ParameterList;
import com.sap.mw.jco.JCO.Table;

public class JCO_ZXFUN_PM_YFFY {

	private static final String REPOSITORY_NAME = "MYRepository";

	private static final String PARAMETER_COST_CENTER = "TABLE_IN";

	private static final String PARAMETER_MONTH = "PERIO";

	private static final String PARAMETER_YEAR = "GJAHR";

	private static final String FUNCTION_NAME = "ZXFUN_PM_YFFY";

	
	public static Client getSAPClient() {
		SAPConnectionPool connPool = new SAPConnectionPool();
		Client sapClient = connPool.connSAP();
		return sapClient;
	}

	public static void releaseClient(Client sapClient) {
		JCO.releaseClient(sapClient);
	}
	
	/**
	 * 
	 * @param orgCodeArray
	 *            ,公司代码
	 * @param costCodeArray
	 *            ,成本中心代码
	 * @param year
	 *            , 年
	 * @param month
	 *            ,月
	 * @param account
	 *            , 科目
	 * @param costElementArray
	 * @throws Exception
	 */
	public Map<String, Map<String, Double>> getCost(String[] costCodeArray,
			String[] workordersArray, String[] costElementArray, int year,
			int month) throws Exception {

		Client client = getSAPClient();
		IRepository repository = JCO.createRepository(REPOSITORY_NAME,
				client);

		IFunctionTemplate ftemplate = repository
				.getFunctionTemplate(FUNCTION_NAME);

		if (ftemplate == null) {
			throw new IllegalArgumentException(
					"Can not get function template, Name:" + FUNCTION_NAME);
		}

		Map<String, Map<String, Double>> ret = new HashMap<String, Map<String, Double>>();

		Function function = ftemplate.getFunction();
		ParameterList input_variable = function.getImportParameterList();
		input_variable.setValue(year, PARAMETER_YEAR);
		input_variable.setValue(month, PARAMETER_MONTH);

		ParameterList input_table = function.getTableParameterList();
		Table tableIn = input_table.getTable(PARAMETER_COST_CENTER);
		for (int i = 0; costCodeArray != null && i < costCodeArray.length; i++) {
			tableIn.appendRow();
			tableIn.setValue(costCodeArray[i], "KOSTL");// 成本中心
		}

		for (int i = 0; costElementArray != null && i < costElementArray.length; i++) {
			tableIn.appendRow();
			tableIn.setValue(costElementArray[i], "KSTAR");// 成本要素
		}

		for (int i = 0; workordersArray != null && i < workordersArray.length; i++) {
			tableIn.appendRow();
			tableIn.setValue(workordersArray[i], "AUFNR");// 工作令号
		}

		function.setTableParameterList(input_table);

		client.execute(function);
		
		releaseClient(client);
		
		Table result = function.getTableParameterList().getTable("TABLE_OUT");
		if (result.getNumRows() > 0) {
			while (result.nextRow()) {

				Map<String, Object> row = new HashMap<String, Object>();

				for (FieldIterator e = result.fields(); e.hasMoreElements();) {
					JCO.Field field = e.nextField();
					String key = field.getName();
					row.put(key, field.getValue());

				}

				/**
				 * 转置数据
				 */
				String _key = (String) row.get("KOSTL");
				if (Utils.isNullOrEmpty(_key)) {
					_key = (String) row.get("AUFNR");
				}

				String _cost = (String) row.get("WKGBTR");
				String _accountNumber = (String) row.get("KSTAR");

				Map<String, Double> rowData = ret.get(_key);
				if (rowData == null) {
					rowData = new HashMap<String, Double>();
					ret.put(_key, rowData);
				}
				Double cost = 0d;
				if (!Utils.isNullOrEmpty(_cost)) {
					try {
						cost = Double.parseDouble(_cost);
					} catch (Exception e) {
					}
				}
				Double _accountSummay = rowData.get(_accountNumber);
				if (_accountSummay == null) {
					_accountSummay = cost;
				} else {
					_accountSummay += cost;
				}
				rowData.put(_accountNumber, _accountSummay);
				Double _rowSummay = rowData.get("summ");
				if (_rowSummay == null) {
					_rowSummay = cost;
				} else {
					_rowSummay += cost;
				}
				rowData.put("summ", _rowSummay);
			}
		} else{
//			System.out.println( costCodeArray);
		}
		return ret;
	}

}
