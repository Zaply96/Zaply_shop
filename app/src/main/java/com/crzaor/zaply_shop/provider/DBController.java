package com.crzaor.zaply_shop.provider;

import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.model.User;

import org.postgresql.util.Base64;

import java.security.MessageDigest;
import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DBController {
    DBConnection db = DBConnection.getInstance();
    private static final String ENCRYPT_KEY = "holadea";



    public User getUser(String email) {
        db.connect();
        User user = null;
        try {
            ResultSet rs = db.getUser(email);
            if (rs != null && rs.next() && !rs.wasNull()) {
                int user_id =  rs.getInt("id_cliente");
                String name =  rs.getString("nombre");
                String password = decrypt(rs.getString("contrasenya"));
                String phone =  rs.getString("telefono");
//                ArrayList<Integer> favorites = (ArrayList<Integer>) rs.getArray("favorite_products");
//                ArrayList<Integer> card = (ArrayList<Integer>) rs.getArray("card_products");
                user = new User(user_id,name,password,phone,email,null,null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            db.disconnect();
        }
        return user;
    }

    public List<Integer> getFavoriteProductsID(String email) {
        db.connect();
        List<Integer> favorites = new ArrayList<>();
        ResultSet rs = db.getUser(email);
        try {
            if (rs != null && rs.next() && !rs.wasNull()) {
                System.out.println(rs.getInt("id_cliente"));
                Array fav  = rs.getArray("favorite_products");
                Object[] aux = (Object[]) fav.getArray();
                for(Object i: aux){favorites.add(((Number)i).intValue());}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        } finally {
            db.disconnect();
        }
        return favorites;
    }

    public boolean updateCard(String  email, List<Integer> card){
        db.connect();
        boolean status = db.updatecard(email, (ArrayList<Integer>) card);
        db.disconnect();
        return status;
    }

    public List<Integer> getCardProductsId(String email) {
        db.connect();
        ArrayList<Integer> card_products = new ArrayList<>();
        ResultSet rs = db.getUser(email);
        try {
            if (rs != null && rs.next() && !rs.wasNull()) {
                Array fav  = rs.getArray("card_products");
                if(fav == null){return new ArrayList<>();}
                Object[] aux = (Object[]) fav.getArray();
                for(Object i: aux){card_products.add(((Number)i).intValue());}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        } finally {
            db.disconnect();
        }
        return card_products;
    }

    public List<Product> getCardProducts(String email) {
        db.connect();

        List<Product> productList;

        List<Integer> productsId = getCardProductsId(email);
        db.connect();
        ResultSet rs = db.getCardProducts(productsId);
        try {
            productList = getProductList(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            db.disconnect();
        }
        return productList;
    }

    public boolean updateFavorites(String  email, List<Integer> favorites){
        db.connect();
        boolean status = db.updateFavorites(email, (ArrayList<Integer>) favorites);
        db.disconnect();
        return status;
    }

    public boolean insertUser(String name, String email, String password) {
        db.connect();
        boolean inserted = false;
        try {
            password = encript(password);
            inserted = db.insertUser(name, email, password, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }

        return inserted;
    }


    public List<Product> getAllProducts() {
        db.connect();

        List<Product> productList;
        ResultSet rs = db.getAllProducts();
        try {
            productList = getProductList(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            db.disconnect();
        }
        return productList;
    }

    public List<Product> getAllProductsOfCategory(String nameCategory) {
        db.connect();
        List<Product> productList;
        ResultSet rs = db.getProductsOfCategory(nameCategory);
        try {
            productList = getProductList(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            db.disconnect();
        }
        return productList;
    }

    public boolean insertOrder(int id_client, String nameReciever, String direction, String phone, String numcard, String titular,
                               String data, String cvv, ArrayList<Integer> products, double total_price, String state){
        db.connect();
        boolean inserted = false;
        try {
            inserted = db.insertOrder(id_client,nameReciever,direction,phone,encript(numcard),titular,data,cvv,products,total_price,state);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }



        return inserted;
    }

    private List<Product> getProductList(ResultSet rs) throws Exception {
        List<Product> productList = new ArrayList<>();
        while (rs != null && rs.next() && !rs.wasNull()) {
            int id_product = rs.getInt("id_producto");
            String title = rs.getString("titulo");
            String description = rs.getString("titulo");
            int stock = rs.getInt("stock");
            double price = rs.getDouble("precio");
            List<String> categories = getCategoriesOfProduct(id_product);
            List<String> materials = getMaterialsOfProduct(id_product);
            String images = getImagesOfProduct(id_product);
            productList.add(new Product(id_product, title, description, stock, price, materials, categories, images));
        }
        return productList;
    }

    public List<Map<String, String>> getAllCategories() {
        db.connect();
        List<Map<String, String>> categoriesList = new ArrayList<>();

        Map<String, String> category;
        ResultSet rs = db.getAllCategories();
        try {
            while (rs != null && rs.next() && !rs.wasNull()) {
                category = new HashMap<>();
                category.put("name", rs.getString("nombre"));
                category.put("url_image", rs.getString("url_image"));
                categoriesList.add(category);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            db.disconnect();
        }
        return categoriesList;
    }


    private List<String> getCategoriesOfProduct(int id_product) throws Exception {
        List<String> categories = new ArrayList<>();
        ResultSet rs = db.getCategoriesOfProduct(id_product);
        while (rs != null && rs.next() && !rs.wasNull()) {
            categories.add(rs.getString("nombre"));
        }
        return categories;
    }

    private List<String> getMaterialsOfProduct(int id_product) throws Exception {
        List<String> materials = new ArrayList<>();
        ResultSet rs = db.getMaterialsOfProduct(id_product);
        while (rs != null && rs.next() && !rs.wasNull()) {
            materials.add(rs.getString("nombre"));
        }
        return materials;
    }

    private String getImagesOfProduct(int id_product) throws Exception {
//        List<String> images = new ArrayList<>();
//        ResultSet rs = db.getUrlImagesOfProduct(id_product);
//        while (rs != null && rs.next() && !rs.wasNull()) {
//            String url = rs.getString("url");
//            images.add(url);
//        }
        String urlimage = null;
        ResultSet rs = db.getUrlImagesOfProduct(id_product);
        if (rs.next() && !rs.wasNull()) {
            urlimage = rs.getString("url");
        }
        return urlimage;
    }

    private String encript(String text) throws Exception {
        final byte[] bytes = text.getBytes("UTF-8");
        final Cipher aes = getCipher(true);
        final byte[] encrypted = aes.doFinal(bytes);
        return Base64.encodeBytes(encrypted);
    }

    private String decrypt(String crypted) throws Exception {
        byte[] encryptedBytes = Base64.decode(crypted.replace("\n", ""));
        final Cipher aes = getCipher(false);
        final byte[] bytes = aes.doFinal(encryptedBytes);
        final String decrypted = new String(bytes, "UTF-8");
        return decrypted;
    }

    private Cipher getCipher(boolean encryt) throws Exception {

        final MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(ENCRYPT_KEY.getBytes("UTF-8"));
        final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

        final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        if (encryt) {
            aes.init(Cipher.ENCRYPT_MODE, key);
        } else {
            aes.init(Cipher.DECRYPT_MODE, key);
        }

        return aes;
    }
}
