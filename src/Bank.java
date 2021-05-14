import java.util.ArrayList;
import java.util.List;

public class Bank {

    private String name;
    private List<Client> clientList = new ArrayList<>();

    public Bank(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public void addClient(Client c){
        this.clientList.add(c);
    }


    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", clientList=" + clientList +
                '}';
    }
}
