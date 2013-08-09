package com.sg.business.vault.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class SearchJob extends Job {

	private String queryText;
	private ArrayList<PrimaryObject> searchResultData;

	public SearchJob(String name) {
		super("¼ìË÷"+name);
		this.queryText = name;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			doSearch();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
		}
		return Status.OK_STATUS;
	}

	public void doSearch() throws IOException, ParseException {
		File file = new File(IndexJob.indexPath);
		IndexReader reader = DirectoryReader.open(FSDirectory.open(file));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_44, true);
		QueryParser parser = new QueryParser(Version.LUCENE_44, "contents", analyzer);
		Query query = parser.parse(queryText);
		
		TopDocs searchResult = searcher.search(query, 200);
		ScoreDoc[] docs = searchResult.scoreDocs;
		
		searchResultData = new ArrayList<PrimaryObject>();
		for (int i = 0; i < docs.length; i++) {
			Document doc = searcher.doc(docs[i].doc);
			String docId = doc.get("docId");
			String db = doc.get("docDB");
			String col = doc.get("docCollection");
			if(docId==null||db==null||col==null){
				continue;
			}
			Class<? extends PrimaryObject> t = ModelService.getModelClass(db, col);
			PrimaryObject po = ModelService.createModelObject(t, new ObjectId(docId));
			if(po!=null&&!searchResultData.contains(po)){
				searchResultData.add(po);
			}
		}
	}
	
	public ArrayList<PrimaryObject> getSearchResult(){
		return searchResultData;
	}

}
