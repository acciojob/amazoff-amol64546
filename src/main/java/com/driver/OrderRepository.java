package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String,Order> orderDB;
    Map<String,DeliveryPartner> partnerDB;
    Map<String,String> orderPartnerDB;
    Map<String,List<String>> partnerOrderDB;

    public OrderRepository(){
        orderDB = new HashMap<>();
        partnerDB = new HashMap<>();
        orderPartnerDB = new HashMap<>();
        partnerOrderDB = new HashMap<>();
    }

    public void addOrder(Order order){
        orderDB.put(order.getId(), order);

    }
    public void addPartner(String partnerId){
        partnerDB.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        if(orderDB.containsKey(orderId) && partnerDB.containsKey(partnerId)){
            orderPartnerDB.put(orderId,partnerId);

            List<String> list = new ArrayList<>();
            if(partnerOrderDB.containsKey(partnerId)){
                list = partnerOrderDB.get(partnerId);
            }
            list.add(orderId);
            partnerOrderDB.put(partnerId,list);
            // increase no of order for partner
            partnerDB.get(partnerId).setNumberOfOrders(partnerDB.get(partnerId).getNumberOfOrders()+1);
        }

    }

    public Order getOrderById(String orderId){
        return orderDB.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return partnerDB.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        return partnerOrderDB.get(partnerId).size();

    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return partnerOrderDB.get(partnerId);
    }

    public List<String> getAllOrders(){

        return new ArrayList<>(orderDB.keySet());
    }
    public Integer getCountOfUnassignedOrders(){
        return orderDB.size()-orderPartnerDB.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId){
        int ordersLeft = 0;
        for(String orderId: partnerOrderDB.get(partnerId)){
            if(orderDB.get(orderId).getDeliveryTime() > time){
                ordersLeft++;
            }
        }
        return ordersLeft;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId){
        int ans = 0;
        for(String orderId: partnerOrderDB.get(partnerId)){
            ans = Math.max(ans, orderDB.get(orderId).getDeliveryTime());
        }
        return ans;
    }


    public void deletePartnerById(String partnerId){
        partnerDB.remove(partnerId);

        List<String> listOfOrders = partnerOrderDB.get(partnerId);
        partnerOrderDB.remove(partnerId);

        for(String orderId: listOfOrders){
            orderPartnerDB.remove(orderId);
        }


    }
    public void deleteOrderById(String orderId){
        orderDB.remove(orderId);

        String partnerId = orderPartnerDB.get(orderId);
        orderPartnerDB.remove(orderId);

        partnerOrderDB.get(partnerId).remove(orderId);

        partnerDB.get(partnerId).setNumberOfOrders(partnerOrderDB.get(partnerId).size());


    }

}
