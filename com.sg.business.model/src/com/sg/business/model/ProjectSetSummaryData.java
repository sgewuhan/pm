package com.sg.business.model;

public class ProjectSetSummaryData {

	private int total;

	private int finished;

	private int processing;

	private int processing_normal;

	private int processing_delay;

	private int processing_advance;

	private int finished_normal;

	private int finished_delay;

	private int finished_advance;

	public ProjectSetSummaryData(int processing_normal, int processing_delay,
			int processing_advance, int finished_normal, int finished_delay,
			int finished_advance) {
		this.processing_advance = processing_advance;
		this.processing_delay = processing_delay;
		this.processing_normal = processing_normal;
		this.processing = processing_advance + processing_delay
				+ processing_normal;

		this.finished_advance = finished_advance;
		this.finished_delay = finished_delay;
		this.finished_normal = finished_normal;
		this.finished = finished_advance + finished_delay + finished_normal;
	}

	public int getTotal() {
		return total;
	}

	public int getFinished() {
		return finished;
	}

	public int getProcessing() {
		return processing;
	}

	public int getProcessing_normal() {
		return processing_normal;
	}

	public int getProcessing_delay() {
		return processing_delay;
	}

	public int getProcessing_advance() {
		return processing_advance;
	}

	public int getFinished_normal() {
		return finished_normal;
	}

	public int getFinished_delay() {
		return finished_delay;
	}

	public int getFinished_advance() {
		return finished_advance;
	}

	
	
}
