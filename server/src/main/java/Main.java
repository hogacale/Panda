import static spark.Spark.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.*;

public class Main {
	private static String distributorsPath = "resources/Distributors.xlsx";
	private static String inventoryPath = "resources/Inventory.xlsx";
    public static void main(String[] args) {

        //This is required to allow GET and POST requests with the header 'content-type'
        options("/*",
                (request, response) -> {
                        response.header("Access-Control-Allow-Headers",
                                "content-type");

                        response.header("Access-Control-Allow-Methods",
                                "GET, POST");


                    return "OK";
                });

        //This is required to allow the React app to communicate with this API
        before((request, response) -> response.header("Access-Control-Allow-Origin", "http://localhost:3000"));

        //TODO: Return JSON containing the candies for which the stock is less than 25% of it's capacity
        get("/low-stock", (request, response) -> {
        	ArrayList<String> jsonData = new ArrayList<String>();
        	String finalString = "[";
        	File inventoryFile = new File(inventoryPath);
        	
        	try {
    			FileInputStream fis = new FileInputStream(inventoryFile);
    			XSSFWorkbook workbook = new XSSFWorkbook(fis);
    	    	XSSFSheet sheet = workbook.getSheetAt(0);
    	    	Iterator<Row> rowInv = sheet.iterator();
    	    	rowInv.next();
    	    	
    	    	while(rowInv.hasNext()) {
    	    		Row row = rowInv.next();
    	    		
    	    		Iterator<Cell> invCellIterator = row.cellIterator();
//    	    		Name | Stock | Capacity | ID
    	    		String[] tempData = new String[4];
    	    		int i = 0;
    	    		while(invCellIterator.hasNext()) {
    	    			Cell cell = invCellIterator.next();
    	    			tempData[i] = cell.toString();
    	    			i++;
    	    		}
    	    		
//    	    		Choose whether to add the data to the dataset or not
    	    		float percentage = Float.parseFloat(tempData[1]) / Float.parseFloat(tempData[2]);
//    	    		System.out.println("Percentage is " + percentage);
    	    		if(percentage < 0.25) {
    	    			jsonData.add("{\"name\":\"" + tempData[0] + "\",\"Stock\":\"" + tempData[1] + "\",\"Capacity\":\""+tempData[2]+"\",\"ID\":\""+tempData[3]+"\"}");
    	    		}
    	    	}
    	    	for(String item: jsonData) {
    	    		finalString += (item);
    	    		if(item.equals(jsonData.get(jsonData.size()-1))) {
    	    			break;
    	    		}
    	    		finalString += ",";
    	    	}
    	    	finalString += ("]");
    	    	System.out.println(finalString);
    	    	workbook.close();
    	    	return finalString;
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
			return -1;
        });

        //TODO: Return JSON containing the total cost of restocking candy
//        I decided to return the JSON containing the total price of each item and it's associated id
        post("/restock-cost", (request, response) -> {
        	JSONArray jaInput = new JSONArray(request.body());
        	JSONObject joInput = new JSONObject();
        	JSONArray output = new JSONArray();
        	
        	HashMap<String,String> idList = new HashMap<String,String>();
        	HashMap<String,String> amountList = new HashMap<String,String>();
        	String stringIdList[] = new String[jaInput.length()];
        	float objectPrice = 0;
        	
        	for(int i = 0; i < jaInput.length(); i++) {
        		joInput = jaInput.getJSONObject(i);
        		idList.put(joInput.getString("id"), "1000000");
        		amountList.put(joInput.getString("id"), joInput.getString("amount"));
        		stringIdList[i] = joInput.getString("id");
        	}
        	

        	for(int i = 0; i < 3; i++) {
        		lowestPrice(idList,i);
        	}

        	JSONObject outputObj = new JSONObject();

        	for(int i = 0; i < idList.size(); i++) {
        		outputObj.put("id",stringIdList[i]);
        		objectPrice = Float.parseFloat(amountList.get(stringIdList[i])) * Float.parseFloat(idList.get(stringIdList[i]));
        		objectPrice = (float) Math.round(objectPrice * 100) / 100;
        		outputObj.put("price", objectPrice);
        		output.put(outputObj);
        		outputObj = new JSONObject();
        	}
        	System.out.println(output.toString());
//        	System.out.println(jo.get("amount"));
            return output.toString();
        });

    }
    
    private static void lowestPrice(HashMap<String,String> id, int page) {
    	File distributorFile = new File(distributorsPath);
    	
    	try {
			FileInputStream fis = new FileInputStream(distributorFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			XSSFSheet sheet = workbook.getSheetAt(page);
			
		    	Iterator<Row> rowDis = sheet.iterator();
		    	rowDis.next();
		    	int i = 1;
		    	while(rowDis.hasNext()) {
		    		Row row = rowDis.next();
//		    		Thinks there are infinite values in the xlsx file
		    		if(row.getCell(0) == null) {
		    			System.out.println("Stopping");
		    			return;
		    		}
		    		Iterator<Cell> invCellIterator = row.cellIterator();

		    		invCellIterator.next();
		    		Cell idCell = invCellIterator.next();
		    		Cell costCell = invCellIterator.next();
		    		
		    		if(id.containsKey(idCell.toString())) {
//		    			System.out.println("Found" + Float.parseFloat(id.get(idCell.toString())));
		    			
			    		if(Float.parseFloat(id.get(idCell.toString())) > Float.parseFloat(costCell.toString())) {
			    			
			    			id.put(idCell.toString(), costCell.toString());
//			    			System.out.println("Changed");
			    		}
		    		}
		    	}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

