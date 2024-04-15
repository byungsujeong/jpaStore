package jpapaper.jpastore.service;

import jpapaper.jpastore.domain.Delivery;
import jpapaper.jpastore.domain.Item.Item;
import jpapaper.jpastore.domain.Member;
import jpapaper.jpastore.domain.Order;
import jpapaper.jpastore.domain.OrderItem;
import jpapaper.jpastore.repository.ItemRepository;
import jpapaper.jpastore.repository.MemberRepository;
import jpapaper.jpastore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
    * 주문
    */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // Entity 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAdress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        
        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문저장
        orderRepository.save(order);

        return order.getId();

    }

    /*
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        
        // 주문 취소
        order.cancel();

    }

    // 검색
    /*public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }*/

}