package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap;
    Map<String,DeliveryPartner> partnerMap;
    Map<String,String> pairMap;
    Map<String,List<String>> partnerOrders;

    public OrderRepository(){
        orderMap = new HashMap<>();
        partnerMap = new HashMap<>();
        pairMap = new HashMap<>();
        partnerOrders = new HashMap<>();
    }

    public void addOrder(Order order){
        orderMap.put(order.getId(), order);

    }
    public void addPartner(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        List<String> orderList = partnerOrders.getOrDefault(partnerId,new ArrayList<>());
        orderList.add(orderId);
        partnerOrders.put(partnerId,orderList);

        pairMap.put(orderId,partnerId);

        partnerMap.get(partnerId).setNumberOfOrders(orderList.size());
    }

    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return partnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        return partnerMap.get(partnerId).getNumberOfOrders();

    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return partnerOrders.get(partnerId);
    }

    public List<String> getAllOrders(){

        return new ArrayList<>(orderMap.keySet());
    }
    public Integer getCountOfUnassignedOrders(){
        return orderMap.size()-pairMap.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        int ordersLeft = 0;
        int timeInt = Integer.parseInt(time.substring(0,2))*60 + Integer.parseInt(time.substring(3));
        for(String orderId: partnerOrders.get(partnerId)){
            if(orderMap.get(orderId).getDeliveryTime() > timeInt){
                ordersLeft++;
            }
        }
        return ordersLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int ans = 0;
        for(String orderId: partnerOrders.get(partnerId)){
            ans = Math.max(ans, orderMap.get(orderId).getDeliveryTime());
        }
        return timeIntToString(ans);
    }
    public String timeIntToString(int time){
        int hr = time%60;
        int min = time/60;

        String ans = "";

        if(hr<10) ans += "0"+hr;
        else ans += hr;

        ans += ":";

        if(min<10) ans += "0"+min;
        else ans += min;

        return ans;

    }

    public void deletePartnerById(String partnerId){
        for(String orderId: partnerOrders.get(partnerId)){
            pairMap.remove(orderId);
        }
        partnerOrders.remove(partnerId);
        partnerMap.remove(partnerId);
    }
    public void deleteOrderById(String orderId){
        orderMap.remove(orderId);

        if(pairMap.containsKey(orderId)){
            String partnerId = pairMap.get(orderId);
            pairMap.remove(orderId);
            partnerOrders.get(partnerId).remove(orderId);
            partnerMap.get(partnerId).setNumberOfOrders(partnerOrders.get(partnerId).size());
        }
    }

}
