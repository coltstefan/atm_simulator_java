

import javax.sound.midi.Soundbank;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class ATM {

    private Bank bank;
    private double amountRon = 9000;
    private double amountDollars = 10000;
    private double amountEuro = 10000;
    private Client client;
    private String[] monede = {"RON" , "USD" , "EURO"};

    public ATM(Bank bank,Client client) {
        this.bank = bank;
        this.client = client;
    }


    public void run() throws IOException {


        List<String> choices = new ArrayList<>();
        choices.add("Interogare sold");
        choices.add("Retragere ");
        choices.add("Depunere ");
        choices.add("Exchange ");

        List<String> choicesGuest = new ArrayList<>();
        choicesGuest.add("Retragere ");
        choicesGuest.add("Exchange ");



        int choice = 0;
        boolean guest = false;

       getCurrentClient();

       if(!bank.getClientList().contains(client) && client.isActive())
       {
           guest = true;


           try{ while(choice !=4) {
               choice = getOptionsGuest();
               System.out.println("Ati ales " + choicesGuest.get(choice));

               if (choice == 0) {
                   retragereSoldGuest();
               }
               else{
                   if(choice == 1) exchange();
               }

           }} catch (IndexOutOfBoundsException e) {
               System.out.println("Multumim!");
           }

       }
       else {

           if (client.getUniCod().equals("ADMIN") || client.getUniCod().equals("admin")) {
               getAdminConsole();

           } else {
               try {
                   while (choice != 4 && client.isActive()) {
                       choice = getOptionsStart();
                       System.out.println("Ati ales " + choices.get(choice));

                       if (choice == 0) {
                           interogareSold();

                       } else if (choice == 1) {
                           retragereSold();
                       } else if (choice == 2) {
                           depunereSold();
                       } else {
                           exchange();
                       }

                   }
               } catch (IndexOutOfBoundsException e) {
                   System.out.println("Multumim!");
               }
           }
       }


    }

    private void getAdminConsole() throws IOException {

        System.out.println("Enter admin commands");
        System.out.println(">");

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();


        while (!command.equals("CLOSE")){
            if(command.contains("TRACE")){
                String user = command.substring(12);
                searchLog(user);

            }
            if(command.contains("ADD_MONEY")){
                String sum = command.substring(16);
                this.amountRon = this.amountRon + Integer.parseInt(sum);
                System.out.println("Ati adaugat " + sum + "RON in aparatul ATM. " + " TOTAL ATM: " + this.amountRon + "RON");

            }
            if(command.contains("UNLOCK")){
                String user = command.substring(13);
                unlockUser(user);

            }



            System.out.println("Enter admin commands");
            System.out.println(">");
            command = scanner.nextLine();

        }
    }




    private void retragereSoldGuest() throws IOException {


        System.out.println("Deoarece nu sunteti un client al bancii, vi se va percepe o taxa de 10% din suma retrasa");
        System.out.println("Sunteti de acord: 1.Yes  2.No");
        Scanner scanner = new Scanner(System.in);
        int alegere_comision = scanner.nextInt();


        if(alegere_comision == 1) {
            System.out.println("Selectati currency: 1.RON , 2.USD , 3.Euro");
            System.out.print(">");
            // String client_file = "src\\client_log.txt";


            // Scanner scanner = new Scanner(System.in);

            int alegereMon = scanner.nextInt();

            if (alegereMon == 1) {
                double max = this.amountRon;
                if (this.amountRon < 10000) {

                    max = 0.1 * this.amountRon;
                    System.out.println("Din pacate, stocul de cash de pe acest aparat ATM este redus, astfel suma maxima pe care o puteti retrage este " + max + monede[alegereMon - 1]);
                }
                System.out.print("Inserati suma (multiplu de 10): ");
                double suma = scanner.nextInt();
                suma = suma - 0.01*suma;


                if (suma <= max && suma > 30 && suma < client.getAmountOfMoney()) {
                    this.amountRon = this.amountRon - suma;
                    client.setAmountOfMoney(client.getAmountOfMoney() - suma);
                    String log = "Guest cu id " + client.getUniCod() + " a retras suma de " + suma + " " + monede[alegereMon - 1];
                    System.out.println(log);
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\guest_log.txt", true))) {
                        bw.write(log);
                        bw.newLine();
                        bw.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    System.out.println("Suma este prea mare");
                }


            }

            if (alegereMon == 2) {
                System.out.println("!Atentie! Cursul valutar curent: 1 RON = " + App.cursValutar[0][1] + " USD");
                System.out.print("Inserati suma (multiplu de 10): ");
                double suma_dolari = scanner.nextDouble();
                double suma = suma_dolari * App.cursValutar[1][0];
                double max = this.amountRon;
                if (this.amountDollars < 1000) {
                    max = 0.1 * this.amountRon;
                }

                System.out.println("dolari:" + suma_dolari);
                System.out.println("lei:" + suma);


                if (suma <= max && suma > 30 && suma < client.getAmountOfMoney()) {
                    this.amountDollars = this.amountDollars - suma_dolari;
                    client.setAmountOfMoney(client.getAmountOfMoney() - suma);
                    String log = "Clientul cu id " + client.getUniCod() + " a retras suma de " + suma_dolari + " " + monede[alegereMon - 1];
                    System.out.println(log);
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt", true))) {
                        bw.write(log);
                        bw.newLine();
                        bw.flush();

                    }


                } else {
                    System.out.println("Suma este prea mare");
                }
            }

            if (alegereMon == 3) {
                System.out.println("!Atentie! Cursul valutar curent: 1 RON = " + App.cursValutar[0][2] + " EURO");
                System.out.print("Inserati suma (multiplu de 10): ");
                double suma_euro = scanner.nextDouble();
                double suma = suma_euro * App.cursValutar[2][0];
                double max = this.amountEuro;
                if (this.amountEuro < 1000) {
                    max = (10 / 100) * this.amountEuro;
                }

                System.out.println("euro:" + suma_euro);
                System.out.println("lei:" + suma);


                if (suma <= max && suma > 30 && suma < client.getAmountOfMoney()) {
                    this.amountEuro = this.amountEuro - suma_euro;
                    client.setAmountOfMoney(client.getAmountOfMoney() - suma);
                    String log = "Clientul cu id " + client.getUniCod() + " a retras suma de " + suma_euro + " " + monede[alegereMon - 1];
                    System.out.println(log);
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt", true))) {
                        bw.write(log);
                        bw.newLine();
                        bw.flush();

                    }


                } else {
                    System.out.println("Suma este prea mare");
                }
            }
        }
        else {
            System.out.println("Multimim pt raspuns!");
        }

    }

    private int getOptionsGuest() {
        System.out.println("0.Retragere");
        System.out.println("1.Exchange");
        System.out.print(">");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        //scanner.close();
        return choice;
    }

    private void exchange() {

        System.out.println("Selectati currency you have: 1.RON , 2.USD , 3.Euro");
        System.out.print(">");
        // String client_file = "src\\client_log.txt";

        Scanner scanner = new Scanner(System.in);

        int alegereMon_client = scanner.nextInt()-1;

        System.out.println("Selectati currency you want: 1.RON , 2.USD , 3.Euro");
        System.out.print(">");

        int aleregeMon_exch = scanner.nextInt()-1;

        System.out.println("Introduceti suma de " + monede[alegereMon_client] + " pe care doriti sa o schimbati in " + monede[aleregeMon_exch]);
        System.out.print(">");

        double suma_data = scanner.nextDouble();
        double suma_primita  = suma_data * App.cursValutar[alegereMon_client][aleregeMon_exch];
//
        String log  = "Clientul cu id " + client.getUniCod() + " a schimbat suma de " + suma_data + monede[alegereMon_client] + " in "+ suma_primita + monede[aleregeMon_exch];
        System.out.println(log);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
            bw.write(log);
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


        if(alegereMon_client==0){this.amountRon = this.amountRon + suma_data;}
        if(alegereMon_client==1){this.amountDollars = this.amountDollars + suma_data;}
        if(alegereMon_client==2){this.amountEuro = this.amountEuro + suma_data;}

        if(aleregeMon_exch==0){this.amountRon = this.amountRon - suma_primita;}
        if(aleregeMon_exch==1){this.amountDollars = this.amountDollars - suma_primita;}
        if(aleregeMon_exch==2){this.amountEuro = this.amountEuro - suma_primita;}






    }

    private void depunereSold() {

        System.out.println("Selectati currency: 1.RON , 2.USD , 3.Euro");
        System.out.print(">");
        // String client_file = "src\\client_log.txt";



        Scanner scanner = new Scanner(System.in);

        int alegereMon = scanner.nextInt();

        if(alegereMon==1){
            System.out.println("Introduceti suma pe care doriti sa o depuneti");
            int suma_depusa = scanner.nextInt();
            client.setAmountOfMoney(client.getAmountOfMoney()+suma_depusa);
            String log  = "Clientul cu id " + client.getUniCod() + " a depus suma de " + suma_depusa + " " + monede[alegereMon-1];
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
                bw.write(log);
                bw.newLine();
                bw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(alegereMon==2){
            System.out.println("Introduceti suma in dolari pe care doriti sa o depuneti");
            int suma_depusa = scanner.nextInt();
            double suma_lei = suma_depusa*App.cursValutar[1][0];
            client.setAmountOfMoney(client.getAmountOfMoney()+suma_lei);
            System.out.println("Suma depusa in dolari: " + suma_depusa);
            System.out.println("Suma depusa in lei: " + suma_lei);
            String log  = "Clientul cu id " + client.getUniCod() + " a depus suma de " + suma_depusa + " " + monede[alegereMon-1] + " echivalentul a " + suma_lei + monede[0];
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
                bw.write(log);
                bw.newLine();
                bw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(alegereMon==3){
            System.out.println("Introduceti suma in euro pe care doriti sa o depuneti");
            int suma_depusa = scanner.nextInt();
            double suma_lei = suma_depusa*App.cursValutar[2][0];
            client.setAmountOfMoney(client.getAmountOfMoney()+suma_lei);
            System.out.println("Suma depusa in euro: " + suma_depusa);
            System.out.println("Suma depusa in lei: " + suma_lei);
            String log  = "Clientul cu id " + client.getUniCod() + " a depus suma de " + suma_depusa + " " + monede[alegereMon-1] + " echivalentul a " + suma_lei + monede[0];
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
                bw.write(log);
                bw.newLine();
                bw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void getCurrentClient() {

        boolean logged = false;

        while (logged == false) {
            System.out.println("Insereaza userId:");
            Scanner scanner = new Scanner(System.in);
            String uid = scanner.nextLine();
            Boolean flag = false;
            for (Client c : App.clientList) {
                if (c.getUniCod().equals(uid)) {

                    if(c.isActive())
                    { System.out.println("Inserati parola: ");
                        String password = scanner.nextLine();

                        if(password.equals(c.getPin())){
                            System.out.println("Logged in successfully");
                            logged  = true;
                            this.client = c;
                        }
                        else{
                            System.out.println("User or password wrong");
                        }

                    }
                    else{
                        System.out.println("Contul nu este activ");
                    }
                }
                else{
                    StringBuilder input = new StringBuilder();
                    input.append(uid);
                    if(c.getUniCod().equals(input.reverse().toString())){

                        c.setActive(false);
                        System.out.println("Contul cu id " + uid + " a fost dezactivat");
                        logged=true;
                    }


                }
            }
        }

    }

    private void retragereSold() throws IOException {

        System.out.println("Selectati currency: 1.RON , 2.USD , 3.Euro");
        System.out.print(">");
       // String client_file = "src\\client_log.txt";



        Scanner scanner = new Scanner(System.in);

        int alegereMon = scanner.nextInt();

        if(alegereMon == 1){
            double max = this.amountRon;
            if(this.amountRon<10000) {

                max = 0.1*this.amountRon ;
                System.out.println("Din pacate, stocul de cash de pe acest aparat ATM este redus, astfel suma maxima pe care o puteti retrage este " + max + monede[alegereMon-1]);
            }
            System.out.print("Inserati suma (multiplu de 10): ");
            double suma = scanner.nextInt();
            retragereLeiClient(suma,max);



        }

        if(alegereMon == 2){
            System.out.println("!Atentie! Cursul valutar curent: 1 RON = " + App.cursValutar[0][1] + " USD");
            System.out.print("Inserati suma (multiplu de 10): ");
            double suma_dolari = scanner.nextDouble();
            double suma = suma_dolari*App.cursValutar[1][0];
            double max = this.amountRon;
            if(this.amountDollars<1000) { max = 0.1*this.amountRon ; }

            System.out.println("dolari:" + suma_dolari );
            System.out.println("lei:" + suma );

            retragereDolariClient(suma , max , suma_dolari);



        }

        if(alegereMon == 3){
            System.out.println("!Atentie! Cursul valutar curent: 1 RON = " + App.cursValutar[0][2] + " EURO");
            System.out.print("Inserati suma (multiplu de 10): ");
            double suma_euro = scanner.nextDouble();
            double suma = suma_euro*App.cursValutar[2][0];
            double max = this.amountEuro;
            if(this.amountEuro<1000) { max = (10/100)*this.amountEuro ; }

            System.out.println("euro:" + suma_euro );
            System.out.println("lei:" + suma );
            retragereEuroClient(suma,max,suma_euro);


        }





    }

    public void retragereLeiClient(double suma, double max) throws IOException {
        if(suma<=max && suma>30 && suma%10==0 && suma < client.getAmountOfMoney()){
            this.amountRon = this.amountRon - suma;
            client.setAmountOfMoney(client.getAmountOfMoney()-suma);
            String log  ="Clientul cu id " + client.getUniCod() + " a retras suma de " + suma + " " + monede[0];
            System.out.println(log);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
                bw.write(log);
                bw.newLine();
                bw.flush();

            }


        }
        else {
            System.out.println("Suma este prea mare");
        }
    }

    public void retragereDolariClient(double suma, double max, double suma_dolari) throws IOException {
        if(suma<=max && suma>30 && suma < client.getAmountOfMoney()){
            this.amountDollars = this.amountDollars - suma_dolari;
            client.setAmountOfMoney(client.getAmountOfMoney()-suma);
            String log  = "Clientul cu id " + client.getUniCod() + " a retras suma de " + suma_dolari + " " + monede[1];
            System.out.println(log);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
                bw.write(log);
                bw.newLine();
                bw.flush();

            }


        }
        else {
            System.out.println("Suma este prea mare");
        }
    }

    public void retragereEuroClient(double suma, double max, double suma_euro) throws IOException {

        if(suma<=max && suma>30 && suma < client.getAmountOfMoney()){
            this.amountEuro = this.amountEuro - suma_euro;
            client.setAmountOfMoney(client.getAmountOfMoney()-suma);
            String log  = "Clientul cu id " + client.getUniCod() + " a retras suma de " + suma_euro + " " + monede[2];
            System.out.println(log);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\client_log.txt",true))) {
                bw.write(log);
                bw.newLine();
                bw.flush();

            }


        }
        else {
            System.out.println("Suma este prea mare");
        }
    }

    private void interogareSold() {
        System.out.println("Dispuneti de " + client.getAmountOfMoney() + " in contul dumneavoastra");

    }

    private int getOptionsStart() {
        System.out.print("0.Interogare sold        ");
        System.out.println("1.Retragere");
        System.out.print("2.Depunere               ");
        System.out.println("3.Exchange");
        System.out.print(">");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        //scanner.close();
        return choice;

    }

    private void searchLog(String user) throws IOException {
        String file = "src\\client_log.txt";
        String line;
        BufferedReader reader = null;
        try{

            reader = new BufferedReader( new FileReader(file));
            while ((line = reader.readLine()) != null){

                if(line.contains("id "+user)){
                    System.out.println(line);
                }

            }


        } catch (Exception e){
            e.printStackTrace();

        }finally {
            reader.close();
            // System.out.println(bank.getClientList());


        }

    }
    private void unlockUser(String user) {

        for(Client c : App.clientList){
            if(c.getUniCod().equals(user)){
                if(this.bank.getClientList().contains(c)){
                    if(c.isActive()){
                        System.out.println("Contul este deja activ");
                    }
                    else{
                        c.setActive(true);
                        System.out.println("Contul cu id " + user + " a fost activat");
                    }
                }
                else{
                    System.out.println("Contul nu este un client al bancii");
                }
            }

        }

    }



    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public double getAmountRon() {
        return amountRon;
    }

    public void setAmountRon(int amountRon) {
        this.amountRon = amountRon;
    }

    public double getAmountDollars() {
        return amountDollars;
    }

    public void setAmountDollars(int amountDollars) {
        this.amountDollars = amountDollars;
    }

    public double getAmountEuro() {
        return amountEuro;
    }

    public void setAmountEuro(int amountEuro) {
        this.amountEuro = amountEuro;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
