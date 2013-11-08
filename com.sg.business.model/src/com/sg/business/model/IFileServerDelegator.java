package com.sg.business.model;

import java.util.List;

import com.mobnut.db.file.IServerFile;
import com.mobnut.db.model.PrimaryObject;

public interface IFileServerDelegator {

	List<IServerFile> getFiles(PrimaryObject document);

}
