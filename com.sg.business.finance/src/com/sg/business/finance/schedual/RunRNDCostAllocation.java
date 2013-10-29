package com.sg.business.finance.schedual;

public class RunRNDCostAllocation implements Runnable {

	@Override
	public void run() {
		try {
			Thread.currentThread().wait(5000);
			System.out.println("done");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
