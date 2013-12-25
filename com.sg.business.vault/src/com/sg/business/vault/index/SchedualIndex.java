package com.sg.business.vault.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;

public class SchedualIndex implements ISchedualJobRunnable {

	private DBCollection collection;
	private String field;
	private DB db;
	private String dbName;
	private String collectionName;
	public static String indexPath = System.getProperty("user.dir") //$NON-NLS-1$
			+ File.separator + "index"; //$NON-NLS-1$

	public SchedualIndex() {
		this.dbName = IModelConstants.DB;
		this.collectionName = IModelConstants.C_DOCUMENT;
		this.db = DBActivator.getDB(dbName);
		this.collection = this.db.getCollection(collectionName);
		this.field = com.sg.business.model.Document.F_VAULT;
		File file = new File(indexPath);
		if (!file.isDirectory()) {
			file.mkdir();
		}
	}

	@Override
	public boolean run() throws Exception {
		try {
			build();
		} catch (IOException e) {
			throw e;
		}

		return true;
	}

	public void build() throws IOException {
		Directory dir = FSDirectory.open(new File(indexPath));
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_44, true);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_44,
				analyzer);
		// Create a new index in the directory, removing any
		// previously indexed documents
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(dir, iwc);
		indexDocs(writer);
		writer.close();

	}

	private void indexDocs(IndexWriter writer) {
		// 获取文件
		DBCursor cur = collection.find(null,
				new BasicDBObject().append(field, 1));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			BasicDBList fRef = (BasicDBList) dbo.get(field);
			if (fRef != null && fRef.size() > 0) {
				for (int i = 0; i < fRef.size(); i++) {
					DBObject fileRefData = (DBObject) fRef.get(i);
					try {
						String content = FileUtil.getContent(fileRefData);
						indexContent(writer, content,
								((ObjectId) dbo.get("_id")).toString(), //$NON-NLS-1$
								(String) fileRefData.get(RemoteFile.F_DB),
								(String) fileRefData
										.get(RemoteFile.F_NAMESPACE),
								((ObjectId) fileRefData.get(RemoteFile.F_ID))
										.toString(),
								(String) fileRefData.get(RemoteFile.F_FILENAME));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void indexContent(IndexWriter writer, String content, String docId,
			String db, String namespace, String fileId, String filename)
			throws IOException {
		if (content == null)
			return;
		Document doc = new Document();
		System.out.println(filename);

		doc.add(new StringField("docId", docId, Field.Store.YES)); //$NON-NLS-1$
		doc.add(new StringField("docDB", dbName, Field.Store.YES)); //$NON-NLS-1$
		doc.add(new StringField("docCollection", collectionName, //$NON-NLS-1$
				Field.Store.YES));
		doc.add(new StringField("db", db, Field.Store.YES)); //$NON-NLS-1$
		doc.add(new StringField("namespace", namespace, Field.Store.YES)); //$NON-NLS-1$
		doc.add(new StringField("fileId", fileId, Field.Store.YES)); //$NON-NLS-1$
		doc.add(new StringField("filename", filename, Field.Store.YES)); //$NON-NLS-1$
		doc.add(new TextField("contents", content, Field.Store.NO)); //$NON-NLS-1$

		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			writer.addDocument(doc);
		} else {
			writer.updateDocument(new Term("docId", docId), doc); //$NON-NLS-1$
		}

		DBCollection docCol = DBActivator.getCollection(db, collectionName);
		String summary = Utils.getLimitLengthString(content, 500);
		docCol.update(new BasicDBObject().append("_id", new ObjectId(docId)) //$NON-NLS-1$
				.append("_summary", null), new BasicDBObject().append("$set", //$NON-NLS-1$ //$NON-NLS-2$
				new BasicDBObject().append("_summary", summary))); //$NON-NLS-1$
	}
}
