package com.sg.business.commons.eai.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private SAPConnectionPool connPool;

	private Client client;

	private IRepository repository;

	public JCO_ZXFUN_PM_YFFY() {
		connPool = new SAPConnectionPool();
		client = connPool.connSAP();
		repository = JCO.createRepository(REPOSITORY_NAME,
				SAPConnectionPool.POOL_NAME);

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
	 * @throws Exception
	 */
	public List<Map<String, Double>> getJSDZB(String[] orgCodeArray,
			String[] costCodeArray, int year, int month, String[] account)
			throws Exception {
		IFunctionTemplate ftemplate = repository
				.getFunctionTemplate(FUNCTION_NAME);

		if (ftemplate == null) {
			throw new IllegalArgumentException(
					"Can not get function template, Name:" + FUNCTION_NAME);
		}

		List<Map<String, Double>> ret = new ArrayList<Map<String, Double>>();
		try {

			Function function = ftemplate.getFunction();
			ParameterList input_variable = function.getImportParameterList();
			ParameterList input_table = function.getTableParameterList();

			input_variable.setValue(year, PARAMETER_YEAR);
			input_variable.setValue(month, PARAMETER_MONTH);

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
				throw new Exception("函数执行出错" + sMESSAGE);
			}

			Table result = function.getTableParameterList().getTable(
					"TABLE_OUT");
			if (result.getNumRows() > 0) {
				while (result.nextRow()) {

					Map<String, Double> row = new HashMap<String, Double>();

					for (FieldIterator e = result.fields(); e.hasMoreElements();) {
						JCO.Field field = e.nextField();
						String key = field.getName();
						Double value = 0d;
						try {
							value = Double.parseDouble(field.getString());
						} catch (Exception ex) {
						}
						row.put(key, value);

					}

					ret.add(row);
				}
				;
			}
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
