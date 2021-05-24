import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class App {

    public static List<Client> clientList = new ArrayList<>();
    public static double cursValutar[][] = {{0,0,0} , {0,0,0} , {0,0,0}};

    public static void main(String[] args) throws IOException {


        int i =0;


        String file = "src\\data.csv";
        BufferedReader reader = null;
        String line;
        Bank bank = new Bank("BT");


        try{

            reader = new BufferedReader( new FileReader(file));
            while ((line = reader.readLine()) != null){
                String[] row = line.split(",");

                Client c = new Client(row[0],row[1],parseDouble(row[2]));
                if(row[1].charAt(0) == '1' || row[0].charAt(0) == 'A' || row[0].charAt(0)=='a'){
                    bank.addClient(c);
                }
                App.clientList.add(c);


            }


        } catch (Exception e){
            e.printStackTrace();

        }finally {
            reader.close();
           // System.out.println(bank.getClientList());


        }
        reader.close();
        ATM atm = new ATM(bank,bank.getClientList().get(0));

        String exchange_file = "src\\exchange.csv";

        BufferedReader reader_curs = null;
        String line_curs;


        try{

            reader_curs = new BufferedReader( new FileReader(exchange_file));
            while ((line_curs = reader_curs.readLine()) != null){
                String[] row = line_curs.split(",");

                cursValutar[i][0]= Double.parseDouble(row[0]);
                cursValutar[i][1]= Double.parseDouble(row[1]);
                cursValutar[i][2]= Double.parseDouble(row[2]);
                i++;

            }


        } catch (Exception e){
            e.printStackTrace();

        }finally {
            reader.close();
            //System.out.println(bank.getClientList().toString());


        }

        atm.run();


    }

}
