package net.apercova.examples.forkjoin;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Fork-join {@link RecursiveTask} example
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 *
 */
public class ForkJoinTimerTask extends RecursiveTask<Long> {

	private static final long serialVersionUID = 8857065672609958726L;
	private long time;
	private long limit;

	public ForkJoinTimerTask(long time, long limit) {
		super();
		this.time = time;
		this.limit = limit;
	}

	@Override
	protected Long compute() {
		if(time <= limit) {
			return computeDirectly(time);
		} else {
			
			ForkJoinTimerTask r1 = new ForkJoinTimerTask(limit, limit);//computed directly
			ForkJoinTimerTask r2 = new ForkJoinTimerTask( (time - limit), limit);//Recursive task
			
			r2.fork();
			return r1.compute() + r2.join();
		}
		
	}
	
	private Long computeDirectly(long time) {
		try {
			System.out.printf("Sleeping thread %s for %d miliseconds...%n", Thread.currentThread().getName(), time);
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.printf("Can't sleep thread %s.%n", Thread.currentThread().getName());
			e.printStackTrace(System.err);
		}
		return time;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final long limit = 5500;
		final long time = 15000;
		Date d = new Date();
		
		RecursiveTask<Long> task = new ForkJoinTimerTask(time, limit);
		ForkJoinPool pool = new ForkJoinPool(10);
		pool.invoke(task);
		System.out.printf("Result: %d%n", task.get());
		
		pool.shutdown();
		pool.awaitTermination(limit, TimeUnit.MILLISECONDS);
		
		Date d1 = new Date();
		System.out.printf("Elapsed: %s seconds.%n", (d1.getTime()-d.getTime())/1000);
		
	}

}
