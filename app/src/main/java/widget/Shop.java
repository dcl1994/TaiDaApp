package widget;

/**
 * 商品类的详情
 */
public class Shop {
    private String imageUrl;    //图片ID
    private String name;    //商品名称
    private String coment;  //商品描述
    private String price;   //商品价格
    private String number_sold; //商品销售量


    public Shop(String imageUrl,String name,String coment,String price,String number_sold){
        this.imageUrl=imageUrl;
        this.name=name;
        this.coment=coment;
        this.price=price;
        this.number_sold=number_sold;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getName() {
        return name;
    }

    public String getComent() {
        return coment;
    }

    public String getPrice() {
        return price;
    }

    public String getNumber_sold() {
        return number_sold;
    }
}
