public class Client {
    private String UniCod;
    private String pin;
    private double amountOfMoney;
    private boolean active = true;

    public Client() {
    }

    public Client(String uniCod, String pin) {
        UniCod = uniCod;
        this.pin = pin;
    }

    public Client(String uniCod, String pin , double amountOfMoney) {
        UniCod = uniCod;
        this.pin = pin;
        this.amountOfMoney = amountOfMoney;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getUniCod() {
        return UniCod;
    }

    public void setUniCod(String uniCod) {
        UniCod = uniCod;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Client with id " + this.UniCod + " was added to bank with " + this.amountOfMoney + "\n";
    }
}
