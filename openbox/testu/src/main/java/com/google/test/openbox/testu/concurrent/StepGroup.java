package com.google.test.openbox.testu.concurrent;


import com.google.test.openbox.common.algorithm.Homogenizer.Action;
import com.google.test.openbox.common.context.CommonContext;
import com.google.test.openbox.http.Response;
import com.google.test.openbox.http.monitors.CyclicBarrierMonitor;

public abstract class StepGroup implements Action<ConcurrentStep> {

	private String name;
	private int num;
	private CommonContext caseContext;

	public StepGroup(String name, int num, CommonContext caseContext) {
		this.name = name;
		this.num = num;
		this.caseContext = caseContext;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNum() {
		return num;
	}

	public abstract Response run(final CommonContext caseContext,
			final int index, final int counter,
			final CyclicBarrierMonitor cyclicBarrierMonitor);

	@Override
	public ConcurrentStep handle(final int index, final int counter) {
		return new ConcurrentStep(caseContext, index) {

			@Override
			public Response action(CommonContext caseContext,
					CyclicBarrierMonitor cyclicBarrierMonitor) {
				return run(caseContext, index, counter, cyclicBarrierMonitor);
			}
		};
	}

}
