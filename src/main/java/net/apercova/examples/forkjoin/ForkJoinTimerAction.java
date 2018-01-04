package net.apercova.examples.forkjoin;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Fork-join {@link RecursiveAction} example
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 *
 */
public class ForkJoinTimerAction extends RecursiveAction{
	
	private static final long serialVersionUID = 1982720067478475031L;
	private long time;
	private long limit;
	
	public ForkJoinTimerAction(long time, long limit) {
		super();
		this.time = time;
		this.limit = limit;
	}

	@Override
	protected void compute() {
		if(time <= limit) {
			computeDirectly(time);
		} else {
			
			ForkJoinTimerAction r1 = new ForkJoinTimerAction(limit, limit);//computed directly
			ForkJoinTimerAction r2 = new ForkJoinTimerAction( (time - limit), limit);//Recursive action
			
			r2.fork();
			r1.compute();
			r2.join();
			
		}
		
	}
	
	private void computeDirectly(long time) {
		try {
			System.out.printf("Sleeping thread %s for %d miliseconds...%n", Thread.currentThread().getName(), time);
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.printf("Can't sleep thread %s.%n", Thread.currentThread().getName());
			e.printStackTrace(System.err);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final long limit = 5500;
		final long time = 15000;
		Date d = new Date();
		
		RecursiveAction action = new ForkJoinTimerAction(time, limit);
		ForkJoinPool pool = new ForkJoinPool(10);
		pool.invoke(action);
		action.get();
		
		pool.shutdown();
		pool.awaitTermination(limit, TimeUnit.MILLISECONDS);
		
		Date d1 = new Date();
		System.out.printf("Elapsed: %s seconds.%n", (d1.getTime()-d.getTime())/1000);
		
	}

}
