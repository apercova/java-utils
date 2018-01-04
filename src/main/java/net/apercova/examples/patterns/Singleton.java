package net.apercova.examples.patterns;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Diferentes implementaciones de Patron Singleton
 * Test concurrente de instanciación
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 * @see <pre>https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples#thread-safe-singleton</pre>
 *
 */
public class Singleton {
	
	private Singleton(String name) {
		System.out.println(String.format("Created: %s.", String.valueOf(name)));
	}
	
	/**
	 * Eager initialization singleton
	 */
	private static final Singleton eagerInstance = new Singleton("eagerInstance");
	
	public static Singleton getEagerinstance() {
		return eagerInstance;
	}

	/**
	 * Static block initialization
	 */
	private static Singleton staticBlockInstance;
	
	static {
		try {
			staticBlockInstance = new Singleton("staticBlockInstance");
		} catch (Exception e) {
			throw new RuntimeException("Exception occured in creating singleton instance on static block");
		}
	}
	
	public static Singleton getStaticBlockInstance() {
		return staticBlockInstance;
	}
	
	/**
	 * Lazy initialization
	 */
	private static Singleton lazyInitializedInstance;
	
	public static Singleton getLazyInitializedInstance() {
		if(lazyInitializedInstance == null) {
			lazyInitializedInstance = new Singleton("lazyInitializedInstance");
		}
		return lazyInitializedInstance;
	}
	
	/**
	 * Thread safe singleton
	 */
	private static Singleton threadSafeInstance;
	
	public static synchronized Singleton getThreadSafeInstance() {
		if(threadSafeInstance == null) {
			threadSafeInstance = new Singleton("threadSafeInstance");
		}
		return threadSafeInstance;
	}
	
	/**
	 * Thread safe double-checked singleton
	 */
	private static Singleton threadSafeDblChkInstance;
	
	public static Singleton getThreadSafeDblChkInstance() {
		if(threadSafeDblChkInstance == null) {
			synchronized (Singleton.class) {
				if(threadSafeDblChkInstance == null) {
					threadSafeDblChkInstance = new Singleton("threadSafeDblChkInstance");
				}
			}
		}
		return threadSafeDblChkInstance;
	}
	
	/**
	 * Bill Pugh Singleton Implementation
	 */
	private static class SingletonHelper{
        private static final Singleton billPughSingletonInstance = new Singleton("billPughSingletonInstance");
    }
	
	public static Singleton getBillpughsingletoninstance() {
		return SingletonHelper.billPughSingletonInstance;
	}
		

	private static Runnable testSingleton() {
		return new Runnable() {
			
			public void run() {
				System.out.println(String.format("Thread [%s]'s singleton test", Thread.currentThread().getName()));
				Singleton eagerInstance             = Singleton.getEagerinstance();
				Singleton staticBlockInstance       = Singleton.getStaticBlockInstance();
				Singleton lazyInitializedInstance   = Singleton.getLazyInitializedInstance();
				Singleton threadSafeInstance        = Singleton.getThreadSafeInstance();
				Singleton threadSafeDblChkInstance  = Singleton.getThreadSafeDblChkInstance();
				Singleton billPughSingletonInstance = Singleton.getBillpughsingletoninstance();
			}
		};
	}
	
	public static void main(String[] args) {
		final int tnum = 20;
		ExecutorService executor = Executors.newFixedThreadPool(tnum);
		for(int i = 1; i<tnum; i++) {
			executor.execute(testSingleton());
		}
		executor.shutdown();
	}
}
