package com.crzaor.zaply_shop.provider;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBConnection {

    private Connection cn;
    private boolean status;

    private static DBConnection instance;

    final String host = "zaply-shop.c1wmcc8h1bix.eu-west-3.rds.amazonaws.com";
    final String database = "postgres";
    final int port = 5432;
    final String user = "user";
    final String pass = "user";
    String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    public static synchronized DBConnection getInstance(){
        if (instance == null){
            instance = new DBConnection();
        }
        return instance;
    }

    public void connect() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("org.postgresql.Driver");
            cn = DriverManager.getConnection(url, user, pass);
            status = true;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            cn.close();
            status = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet getAllProducts() {
        if (status) {
            Statement st;
            ResultSet rs = null;
            try {

                st = cn.createStatement();
                rs = st.executeQuery("SELECT * FROM productos");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }

    public ResultSet getAllCategories() {
        if (status) {
            Statement st;
            ResultSet rs = null;
            try {

                st = cn.createStatement();
                rs = st.executeQuery("SELECT * FROM categorias");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }

    public ResultSet getProductsOfCategory(String nameCategory) {
        if (status) {
            ResultSet rs = null;
            try {

                PreparedStatement pst = cn.prepareStatement("SELECT p.* FROM productos p \n" +
                        "INNER JOIN producto_categoria pc\n" +
                        "ON pc.id_producto = p.id_producto\n" +
                        "INNER JOIN categorias c\n" +
                        "ON pc.id_categoria = c.id_categoria\n" +
                        "where c.nombre = ?");
                pst.setString(1, nameCategory);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }


    public ResultSet getCategoriesOfProduct(int id_product) {
        if (status) {
            ResultSet rs = null;
            try {

                PreparedStatement pst = cn.prepareStatement("SELECT c.nombre FROM categorias c \n" +
                        "INNER JOIN producto_categoria pc\n" +
                        "ON pc.id_categoria = c.id_categoria\n" +
                        "INNER JOIN productos p\n" +
                        "ON pc.id_producto = p.id_producto\n" +
                        "where p.id_producto = ?");
                pst.setInt(1, id_product);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }

    public ResultSet getMaterialsOfProduct(int id_product) {
        if (status) {
            ResultSet rs = null;
            try {

                PreparedStatement pst = cn.prepareStatement("SELECT m.nombre FROM materiales m \n" +
                        "INNER JOIN producto_material pm\n" +
                        "ON pm.id_material= m.id_material\n" +
                        "INNER JOIN productos p\n" +
                        "ON pm.id_producto = p.id_producto\n" +
                        "where p.id_producto = ?");
                pst.setInt(1, id_product);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }

    public ResultSet getUrlImagesOfProduct(int id_product) {
        if (status) {
            ResultSet rs = null;
            try {

                PreparedStatement pst = cn.prepareStatement("SELECT i.url FROM imagenes i\n" +
                        "where id_producto = ?");
                pst.setInt(1, id_product);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }

    public ResultSet getUser(String email) {
        if (status) {
            ResultSet rs = null;

            try {

                PreparedStatement pst = cn.prepareStatement("SELECT * FROM clientes WHERE email=?");
                pst.setString(1, email);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }
    }

    public ResultSet getCardProducts(List<Integer> card_products){
        if (status) {
            ResultSet rs = null;
            String list ="(";
            for (Integer i: card_products){
                if(card_products.indexOf(i) == card_products.size()-1){
                    list += i+")";
                }else {
                    list += i+",";
                }
            }

            try {
                PreparedStatement pst = cn.prepareStatement("SELECT * FROM productos WHERE id_producto IN "+list);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        } else {
            return null;
        }

    }

    public boolean updateFavorites(String email, ArrayList<Integer> favorites) {
        if (status) {

            try {

                PreparedStatement pst = cn.prepareStatement("UPDATE clientes SET favorite_products = ?" +
                        "where email = ?");
                Array array = cn.createArrayOf("BIGINT", favorites.toArray());
                pst.setArray(1,array);
                pst.setString(2, email);
                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;

    }

    public boolean updatecard(String email, ArrayList<Integer> card_products) {
        if (status) {

            try {

                PreparedStatement pst = cn.prepareStatement("UPDATE clientes SET card_products = ?" +
                        "where email = ?");
                Array array = cn.createArrayOf("BIGINT", card_products.toArray());
                pst.setArray(1,array);
                pst.setString(2, email);
                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;

    }

    public boolean insertUser(String name, String email, String password, String phone) {
        if (status) {

            try {
                PreparedStatement pst = cn.prepareStatement("INSERT INTO clientes (nombre, email, contrasenya, telefono)"
                        + "VALUES (?,?,?,?)");
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, password);
                pst.setString(4, phone);
                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return false;
    }

    public boolean insertOrder(int id_client, String nameReciever, String direction, String phone, String numcard, String titular,
                               String data, String cvv, ArrayList<Integer> products, double total_price, String state){
        if (status) {

            try {
                Array array = cn.createArrayOf("BIGINT", products.toArray());

                PreparedStatement pst = cn.prepareStatement("INSERT INTO pedidos (id_cliente, nombre_receptor,direccion,telefono," +
                        "numero_tarjeta,titular,emision,cvv,productos,precio_total,estado)"
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1,id_client);
                pst.setString(2,nameReciever);
                pst.setString(3,direction);
                pst.setString(4,phone);
                pst.setString(5,numcard);
                pst.setString(6,titular);
                pst.setString(7,data);
                pst.setString(8,cvv);
                pst.setArray(9,array);
                pst.setDouble(10,total_price);
                pst.setString(11,state);
                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return false;
    }
}
