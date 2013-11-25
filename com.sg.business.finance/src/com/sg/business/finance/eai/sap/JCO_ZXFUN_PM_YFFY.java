package com.sg.business.finance.eai.sap;

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
	 * @throws Exception
	 */
	public Map<String, Map<String, Double>> getJSDZB(String[] orgCodeArray,
			String[] costCodeArray, int year, int month, String[] account)
			throws Exception {

		SAPConnectionPool connPool = new SAPConnectionPool();
		Client client = connPool.connSAP();
		IRepository repository = JCO.createRepository(REPOSITORY_NAME,
				SAPConnectionPool.POOL_NAME);

		IFunctionTemplate ftemplate = repository
				.getFunctionTemplate(FUNCTION_NAME);

		if (ftemplate == null) {
			throw new IllegalArgumentException(
					"Can not get function template, Name:" + FUNCTION_NAME);
		}

		Map<String, Map<String, Double>> ret = new HashMap<String, Map<String, Double>>();
		try {

			Function function = ftemplate.getFunction();
			ParameterList input_variable = function.getImportParameterList();
			input_variable.setValue(year, PARAMETER_YEAR);
			input_variable.setValue(month, PARAMETER_MONTH);

			ParameterList input_table = function.getTableParameterList();
			Table tableIn = input_table.getTable(PARAMETER_COST_CENTER);
			for (int i = 0; i < costCodeArray.length; i++) {
				tableIn.appendRow();
				tableIn.setValue(costCodeArray[i], "KOSTL");
			}
			function.setTableParameterList(input_table);

			client.execute(function);

			String sRESULT = function.getExportParameterList().getString(
					"RESULT");
			String sMESSAGE = function.getExportParameterList().getString(
					"MESSAGE"); //
			if (sRESULT.equals("E")) {
				throw new Exception("ERROR: " + sMESSAGE);
			}

			Table result = function.getTableParameterList().getTable(
					"TABLE_OUT");
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
					String _costCenterNumber = (String) row.get("KOSTL");
					String _cost = (String) row.get("WKGBTR");
					String _accountNumber = (String) row.get("KSTAR");

					Map<String, Double> rowData = ret.get(_costCenterNumber);
					if (rowData == null) {
						rowData = new HashMap<String, Double>();
						ret.put(_costCenterNumber, rowData);
					}
					Double cost = 0d;
					if (!Utils.isNullOrEmpty(_cost)) {
						try{
							cost = Double.parseDouble(_cost);
						}catch(Exception e){}
					}
					Double _summay = rowData.get(_accountNumber);
					if(_summay == null){
						_summay = cost;
					}else{
						_summay += cost;
					}
					rowData.put(_accountNumber, _summay);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JCO.releaseClient(client);
		}

		return ret;
	}

	// tableIn.appendRow();
	// tableIn.setValue("E014010000", "KOSTL");
	// tableIn.appendRow();
	// tableIn.setValue("E013040100", "KOSTL");
	// tableIn.appendRow();
	// tableIn.setValue("E013070100", "KOSTL");
	// tableIn.appendRow();
	// tableIn.setValue("E013050100", "KOSTL");
	// tableIn.setValue("TL-10-040", "AUFNR");
	// tableIn.setValue("TL-11-027", "KOSTL");

}
