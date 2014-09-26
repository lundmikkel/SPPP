// For week 5
// sestoft@itu.dk * 2014-09-19

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class TestDownload {

	private static final String[] urls = 
	{ "http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
		"http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
		"http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
		"http://www.ing.dk", "http://www.dtu.dk", "http://www.eb.dk", 
		"http://www.nytimes.com", "http://www.guardian.co.uk", "http://www.lemonde.fr",   
		"http://www.welt.de", "http://www.dn.se", "http://www.heise.de", "http://www.wsj.com", 
		"http://www.bbc.co.uk", "http://www.dsb.dk", "http://www.bmw.com", "https://www.cia.gov" 
	};
	private static final ExecutorService executor = Executors.newWorkStealingPool();

	public static void main(String[] args) throws IOException {
		Timer t = new Timer();
		Map<String, String> pages = getPagesParallel(urls, 200);
		System.out.println(t.check());

		//for(Map.Entry<String, String> entry : pages.entrySet()){
		//	System.out.printf("%-25s %6d\n", entry.getKey(), entry.getValue().length());
		//}
	}

	public static Map<String, String> getPagesParallel(final String[] urls, int maxLines) throws IOException {
		int taskCount = urls.length;
		List<Callable<Map.Entry<String, String>>> tasks = new ArrayList<>(taskCount);

		for (int t = 0; t < taskCount; ++t) {
			final String url = urls[t];
			tasks.add(() -> {
				return new AbstractMap.SimpleEntry<String, String>(url, getPage(url, maxLines));
			});
		}

		Map<String, String> pages = new HashMap<>(taskCount);
		try {
			List<Future<Map.Entry<String, String>>> futures = executor.invokeAll(tasks);
			for (Future<Map.Entry<String, String>> future : futures) {
				Map.Entry<String, String> page = future.get();
				pages.put(page.getKey(), page.getValue());
			}
		}
	    catch (Exception exn) { 
	    	System.out.println("Interrupted: " + exn);
	    }
	    return pages;
	}

	/*public static Map<String, String> getPagesParallel(String[] urls, int maxLines) throws IOException {
		List<Callable<Map<String, String>>> tasks = new ArrayList<Callable<Map<String, String>>>();
		Map<String, String> result = new HashMap<String,String>(urls.length);
		
		for (int t=0; t<urls.length; t++) {
		  tasks.add(new Map<String, String>() { public Map<String, String> call() { 
			return getPages(new String[urls[t]],maxLines);
		  }});
		}
		
		try {
			Map<String, String> temp;
	      	List<Future<Map<String, String>>> futures = executor.invokeAll(tasks);
	  		for (Future<Map<String, String>> fut : futures) {
	  			temp = fut.get();
	    		result.put(temp.getKey(), temp.getValue());
	    	}
	    }
	     catch (InterruptedException exn) { 
	      System.out.println("Interrupted: " + exn);
	    }
	}*/

	public static Map<String, String> getPages(String[] urls, int maxLines) throws IOException {
		Map<String, String> map = new HashMap<String,String>(urls.length);
		for(int i = 0; i<urls.length; ++i){
			map.put(urls[i], getPage(urls[i], maxLines));
		}
		return Collections.unmodifiableMap(map);
	}

	public static String getPage(String url, int maxLines) throws IOException {
		// This will close the streams after use (JLS 8 para 14.20.3):
		try (BufferedReader in 
				 = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<maxLines; i++) {
				String inputLine = in.readLine();
				if (inputLine == null)
					break;
				else
					sb.append(inputLine).append("\n");
			}
			return sb.toString();
		}
	}
}

