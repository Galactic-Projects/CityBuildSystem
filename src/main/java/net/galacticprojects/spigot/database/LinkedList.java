package net.galacticprojects.spigot.database;

import com.zaxxer.hikari.pool.HikariPool;
import net.galacticprojects.spigot.util.DynamicArray;

import java.sql.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntFunction;

public class LinkedList<E> {

    public static final String SELECT_LIST = "SELECT * FROM LinkedList WHERE Id = ?";
    public static final String INSERT_LIST = "INSERT INTO LinkedList (Id, List) VALUES (?, ?)";
    public static final String UPDATE_LIST = "UPDATE LinkedList SET List = ? WHERE Id = ?";

    private SQLDatabase database;

    private DynamicArray<E> list = new DynamicArray<>();

    private HikariPool pool;

    private int id;

    public LinkedList(boolean recovery, int id, E unusableThing, SQLDatabase database) {
        this.database = database;
        this.pool = database.pool;
        if(recovery) {
            list = getList(id);
            if(list == null) {
                this.list = new DynamicArray<>();
                this.list.add(unusableThing);
            }
        }
        this.id = id;
        createTables();
        addDatabase();
    }
    public LinkedList(int id, E unusableThing, SQLDatabase database) {
        this.database = database;
        this.pool = database.pool;
        createTables();
        list = getList(id);
        if(list == null) {
            this.list = new DynamicArray<>();
            this.list.add(unusableThing);
        }
        this.id = id;
        addDatabase();
    }

    public LinkedList(int id, E unusableThing, E object, SQLDatabase database) {
        this.database = database;
        this.pool = database.pool;
            list = getList(id);
            if(list == null) {
                this.list = new DynamicArray<>();
                this.list.add(unusableThing);
            }
        this.list.add(object);
        this.id = id;
        createTables();
        addDatabase();
    }

    public LinkedList(boolean recovery,  E unusableThing, int id, E object, SQLDatabase database) {
        this.database = database;
        this.pool = database.pool;
        if(recovery) {
            list = getList(id);
            if(list == null) {
                this.list = new DynamicArray<>();
                this.list.add(unusableThing);
            }
        }
        this.list.add(object);
        this.id = id;
        createTables();
        addDatabase();
    }

    public void add(E object) {
        list.add(object);
        addDatabase();
    }

    public void add(E[] objects) {
        for(E object : objects) {
            list.add(object);
        }
        addDatabase();
    }

    public DynamicArray<E> getDynamicArray() {
        return list;
    }

    private void createTables() {
        try(Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS LinkedList (Id int(50), List VARCHAR(200), PRIMARY KEY(Id))");
            statement.executeUpdate();
            System.out.println("List Table Successfully created");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertList() {
        try(Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_LIST);
            statement.setInt(1, id);
            statement.setArray(2, connection.createArrayOf("LinkedList", list.asArray()));
            statement.executeUpdate();
            System.out.println("List Table Successfully created");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DynamicArray<E> getList(int id) {
        try(Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_LIST);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                Array array = set.getArray("LinkedList");
                E[] a = (E[]) array.getArray();
                DynamicArray<E> arrayList = new DynamicArray<>();
                for(E b : a) {
                    arrayList.add(b);
                }
                System.out.println("LinkedList id(" + id + ") Successfully recovered");
                return arrayList;
            }
            System.out.println("No LinkedList with id(" + id + ") found, creating a new...");
            return null;
        }catch (SQLException e) {
            System.out.println("An SQLError occurred while recover the LinkedList with id " + id);
            e.printStackTrace();
            return null;
        }
    }

    private void addDatabase() {
        try(Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_LIST);
            statement.setArray(1, connection.createArrayOf("LinkedList", list.asArray()));
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("List successfully Updated");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}

