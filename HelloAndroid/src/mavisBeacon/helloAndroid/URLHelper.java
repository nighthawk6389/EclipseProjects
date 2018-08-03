	package mavisBeacon.helloAndroid;
	
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.net.MalformedURLException;
	import java.net.URL;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.List;
	
	
	public class URLHelper {
		
		public static BufferedReader getURLStream(String restOfURL){
	        URL url = null;
	        try {
	            url = new URL("http://www.sidebump.com/ecarnomics/"+restOfURL);
	        } catch (MalformedURLException ex) {
	            System.out.println("MaformedURLException: " + ex.getMessage());
	            System.exit(1);
	        }
	        
	        BufferedReader stream = null;
			try {
				stream = new BufferedReader(new InputStreamReader(url.openStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}
			return stream;
		}
		
		
		/**
		 * Takes a URL, retrieves its data and puts the data into list where each data
		 * is separated by a newline character
		 * @param stream
		 * @return
		 */
		public static List<String> parseURLDataNewlines(BufferedReader stream){
			
			List<String> list = new ArrayList<String>();
	        
	        String line;
			try {
				while( (line = stream.readLine()) != null ){
				    list.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return list;
		}
		
		/**
		 * See parseURLDataNewLines(BufferedReader)
		 * @param restOfURL
		 * @return
		 */
		public static List<String> parseURLDataNewlines(String restOfURL){
			
			BufferedReader stream = getURLStream(restOfURL);
			return parseURLDataNewlines(stream);
		}
		
		public static String capitalizeAndReplaceUnderscores(String str){
			
			String replaced = URLHelper.replaceAllUnderscoresWithSpaces(str);
			String capitalizedAndReplaced = URLHelper.capitalize(replaced);
			
			return capitalizedAndReplaced;
		}
		
		public static String replaceAllUnderscoresWithSpaces(String str){
			
			return str.replace('_', ' ');		
		}
		
		public static String capitalize(String str){
			
			char array[] = str.toCharArray();
			if(array.length == 0)
				return null;
			String capitalized = "";
			array[0] = Character.toUpperCase(array[0]);
			for(int i = 0; i < array.length; i++ ){
				if(array[i] == ' ' && i+1 < array.length){
					array[i+1] = Character.toUpperCase(array[i+1]);
				}
				capitalized += array[i];
			}
			return capitalized;
		}
	}
