package com.bacq.stockmarket.helper;

import com.bacq.stockmarket.Loop;
import com.bacq.stockmarket.service.OrderManagement;
import com.bacq.stockmarket.service.impl.OrderManagementImpl;
import org.junit.*;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class StockMarketHelperTest {

    private static StockMarketHelper stockMarketHelper;

    private static OrderManagement orderManagement;

    @Rule
    public Loop repeatRule = new Loop();

    private static Map<String, File> inputFileMap = new HashMap<>();

    @BeforeClass
    public static void setup(){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource("./inputs/");
        if (url == null){
            throw new RuntimeException("Test setup failed. No Input found");
        }

        File file = new File(url.getFile());

        for(File file1: Objects.requireNonNull(file.listFiles())){
            inputFileMap.put(file1.getName(), file1);
        }
        System.out.println(inputFileMap);
    }

    @Before
    public void beforeTestSetUp(){
        stockMarketHelper = new StockMarketHelper();
        orderManagement = new OrderManagementImpl();
    }

    @Test
    @Loop.Repeat(times = 3)
    public void takeAndAddStockToOrderBookTest() throws Exception{
        String fileName = inputFileMap.keySet().iterator().next();
        System.out.println("Executing testcase with file: "+fileName);
        Scanner scanner = new Scanner(new FileReader(inputFileMap.get(fileName)));
        StringBuilder populateOutput = new StringBuilder();
        while (true){
            String line = scanner.nextLine();
            if(line.equalsIgnoreCase("output")){
                break;
            }
            String[] input = line.split(" ");
            stockMarketHelper.takeAndAddStockToOrderBook(input, orderManagement);
        }

        while (scanner.hasNext()){
            populateOutput.append(scanner.nextLine()).append("\n");
        }
        Assert.assertEquals(stockMarketHelper.displayOutput(), populateOutput.toString());
        stockMarketHelper.flushOutput();
        inputFileMap.remove(fileName);
        scanner.close();
    }
 }
