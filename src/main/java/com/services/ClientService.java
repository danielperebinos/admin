package com.services;

import com.entities.Client;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class ClientService {

    private Configuration configuration;
    private Session session;

    public ClientService() {
        this.configuration = new Configuration().configure("hibernate.cfg.xml");
        this.loadSession();
    }

    public ClientService(String resource) {
        this.configuration = new Configuration().configure(resource);
        this.loadSession();
    }

    private void loadSession() {
        try {
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            this.session = sessionFactory.openSession();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Client> selectAll() {
        Query<Client> query = session.createQuery("FROM Client", Client.class);
        List<Client> clients = query.getResultList();
        return clients;
    }

    public List<Client> filterByName(String name) {
        try {
            Query<Client> query;
            String queryString = "FROM Client";
            if (name.length() > 0) {
                queryString += " WHERE name LIKE :name";
            }

            query = session.createQuery(queryString, Client.class);

            if (name.length() > 0) {
                query.setParameter("name", "%" + name + "%");
            }

            List<Client> clients = query.getResultList();
            return clients;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean create(String name) {
        try {
            Transaction transaction = session.beginTransaction();
            Client client = new Client();
            client.setName(name);
            session.save(client);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean update(Client client, String name) {
        try {
            Transaction transaction = session.beginTransaction();
            client.setName(name);
            session.update(client);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteMany(List<Client> clients) {
        try {
            Transaction transaction = session.beginTransaction();
            for (Client client : clients) {
                if (client.isSelected()) {
                    session.delete(client);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
