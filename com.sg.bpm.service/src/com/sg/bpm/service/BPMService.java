package com.sg.bpm.service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.UrlResource;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;

public class BPMService {

	private Map<String, KnowledgeBase> knowledgeBaseMap = new ConcurrentHashMap<String, KnowledgeBase>();

	private Map<String, StatefulKnowledgeSession> sessionMap = new ConcurrentHashMap<String, StatefulKnowledgeSession>();

	private String guvnorRoot;

	private EntityManagerFactory emf;

	private String localBPMPath;

	public BPMService(String guvnorRoot, String localBPMPath) {

		this.guvnorRoot = guvnorRoot;
		
		this.localBPMPath = localBPMPath;
	}

	public void init(EntityManagerFactory emf) {

		this.emf = emf;
	}

	public KnowledgeBase getKnowledgeBase(String knowledgebaseName) {

		KnowledgeBase kbase = knowledgeBaseMap.get(knowledgebaseName);
		if (kbase == null) {
			KnowledgeBuilder

			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

			registeResource(kbuilder, knowledgebaseName);

			kbase = kbuilder.newKnowledgeBase();

			knowledgeBaseMap.put(knowledgebaseName, kbase);
		}

		return kbase;

	}

	private void registeResource(KnowledgeBuilder knowledgebuilder, String knowledgebaseName) {

		boolean suc = true;
		try{//���Դ�Զ��ע����Դ
			UrlResource urlResource = (UrlResource) ResourceFactory.newUrlResource(guvnorRoot + knowledgebaseName + "/LATEST/");
			urlResource.setBasicAuthentication("enabled");
			urlResource.setUsername("admin");
			urlResource.setPassword("admin");
			knowledgebuilder.add(urlResource, ResourceType.PKG);
			System.out.println("����Զ��֪ʶ��ɹ�");
		}catch(Exception e){
			suc = false;
			System.out.println("��Զ��֪ʶ�������̶���ʧ��");
		}
		
		if(!suc){
			System.out.println("���Դӷ���������Ŀ¼��Դ");
			//���Դӷ���������Ŀ¼��Դ
			File root = new File(localBPMPath);
			String path = root + "/" +knowledgebaseName.replace(".", "/");
			File[] files = new File(path).listFiles();
			for(int i=0;i<files.length;i++){
				knowledgebuilder.add(ResourceFactory.newFileResource(files[i]), ResourceType.BPMN2);
			}
			System.out.println("���ر���֪ʶ��ɹ�");
		}
		
		
	}
	
	public StatefulKnowledgeSession createSession(String knowledgebaseName) {

		StatefulKnowledgeSession session = sessionMap.get(knowledgebaseName);
		if (session == null) {
			KnowledgeBase kbase = getKnowledgeBase(knowledgebaseName);

			Environment env = KnowledgeBaseFactory.newEnvironment();
			env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
//			env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());
			session = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, env);

			sessionMap.put(knowledgebaseName, session);
		}
		return session;
	}

	public StatefulKnowledgeSession getSession(String knowledgebaseName, int sid) {

		StatefulKnowledgeSession session = sessionMap.get(knowledgebaseName);
		if (session == null) {
			KnowledgeBase kbase = getKnowledgeBase(knowledgebaseName);

			Environment env = KnowledgeBaseFactory.newEnvironment();
			env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
//			env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());

			session = JPAKnowledgeService.loadStatefulKnowledgeSession(sid, kbase, null, env);

			if (session == null) {
				return null;
			}
			sessionMap.put(knowledgebaseName, session);
		}
		return session;
	}


	public void stop() {


	}

}
