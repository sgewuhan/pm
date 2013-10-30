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
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

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

public class IndexJob extends Job {

	private DBCollection collection;
	private String field;
	private DB db;
	private String dbName;
	private String collectionName;
	public static String indexPath = System.getProperty("user.dir")
			+ File.separator + "index";

	public IndexJob(String dbName, String collectionName, String field) {
		super("建立索引");
		this.dbName = dbName;
		this.collectionName = collectionName;
		this.db = DBActivator.getDB(dbName);
		this.collection = this.db.getCollection(collectionName);
		this.field = field;
		File file = new File(indexPath);
		if (!file.isDirectory()) {
			file.mkdir();
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			build();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Status.OK_STATUS;
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
								((ObjectId) dbo.get("_id")).toString(),
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

		doc.add(new StringField("docId", docId, Field.Store.YES));
		doc.add(new StringField("docDB", dbName, Field.Store.YES));
		doc.add(new StringField("docCollection", collectionName,
				Field.Store.YES));
		doc.add(new StringField("db", db, Field.Store.YES));
		doc.add(new StringField("namespace", namespace, Field.Store.YES));
		doc.add(new StringField("fileId", fileId, Field.Store.YES));
		doc.add(new StringField("filename", filename, Field.Store.YES));
		doc.add(new TextField("contents", content, Field.Store.NO));

		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			writer.addDocument(doc);
		} else {
			writer.updateDocument(new Term("docId", docId), doc);
		}

		DBCollection docCol = DBActivator.getCollection(db, collectionName);
		String summary = Utils.getLimitLengthString(content, 500);
		docCol.update(new BasicDBObject().append("_id", new ObjectId(docId))
				.append("_summary", null), new BasicDBObject().append("$set",
				new BasicDBObject().append("_summary", summary)));
	}

}
