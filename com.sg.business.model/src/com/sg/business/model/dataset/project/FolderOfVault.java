package com.sg.business.model.dataset.project;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.dataset.folder.VaultOfOrganization;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class FolderOfVault extends MasterDetailDataSetFactory {

	String userId;
	public FolderOfVault() {
		super(IModelConstants.DB, IModelConstants.C_FOLDER);
		userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

//	@Override
//	public DataSet getDataSet() {
//		List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
//		VaultOfOrganization vaultOfOrganization = new VaultOfOrganization(userId);
//		DataSet dataSet = vaultOfOrganization.getDataSet();
//		Iterator<PrimaryObject> iterator = dataSet.iterator();
//		while(iterator.hasNext()){
//			PrimaryObject po = iterator.next();
//			DBCollection collection = getCollection();
//			DBCursor cur = collection.find(new BasicDBObject().append(Folder.F_PARENT_ID, null).append(
//					Folder.F_ROOT_ID, po.get_id()));
//			while(cur.hasNext()){
//				DBObject folderdb = cur.next();
//				Folder folder = ModelService.createModelObject(folderdb,Folder.class);
//				dataItems.add(folder);
//			}
//		}
//		return new DataSet(dataItems);
//	}
//	
	
	@Override
	public DataSet getDataSet() {
		VaultOfOrganization vaultOfOrganization = new VaultOfOrganization(userId);
		DataSet dataSet = vaultOfOrganization.getDataSet();
		return dataSet;
	}
	

}
