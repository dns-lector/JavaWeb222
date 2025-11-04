package learning.itstep.javaweb222.data.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;


public class CartItem {
    private UUID   id;
    private UUID   cartId;
    private UUID   productId;
    private UUID   discountId;
    private double price;
    private int    quantity;
    private Date   deletedAt;
    
    private Product product;
    
    public static CartItem fromResultSet(ResultSet rs) throws Exception {
        CartItem item = new CartItem();
        item.setId(UUID.fromString(rs.getString("ci_id")));
        item.setCartId(UUID.fromString(rs.getString("ci_cart_id")));
        item.setProductId(UUID.fromString(rs.getString("ci_product_id")));

        String diId = rs.getString("ci_di_id");
        if (diId != null) {
            item.setDiscountId(UUID.fromString(diId));
        }

        item.setQuantity(rs.getInt("ci_quantity"));
        item.setPrice(rs.getDouble("ci_price"));

        Timestamp timestamp;
        timestamp = rs.getTimestamp("ci_deleted_at");
        if (timestamp != null) {
            item.setDeletedAt(new Date(timestamp.getTime()));
        }
        
        try { item.setProduct( Product.fromResultSet(rs) ); }
        catch(Exception ignore){}
        
        return item;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getDiscountId() {
        return discountId;
    }

    public void setDiscountId(UUID discountId) {
        this.discountId = discountId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    
}
