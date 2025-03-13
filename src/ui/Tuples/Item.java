package src.ui.Tuples;

public class Item {
    public int iID;
    public String iname;
    public double price;
    public String description;
    public String catergory;
    public int stock;

    // Constructor
    public Item(int iID, String iname, double price, String description, String catergory, int stock) {
        this.iID = iID;
        this.iname = iname;
        this.price = price;
        this.description = description;
        this.catergory = catergory;
        this.stock = stock;
    }
}

