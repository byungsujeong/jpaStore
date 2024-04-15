package jpapaper.jpastore.service;

import jakarta.persistence.EntityManager;
import jpapaper.jpastore.domain.Address;
import jpapaper.jpastore.domain.Item.Book;
import jpapaper.jpastore.domain.Item.Item;
import jpapaper.jpastore.domain.Member;
import jpapaper.jpastore.domain.Order;
import jpapaper.jpastore.domain.OrderStatus;
import jpapaper.jpastore.exception.NotEnoughStockExceoption;
import jpapaper.jpastore.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test(expected = NotEnoughStockExceoption.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);

        // when
        int orderCount = 11;
        orderService.order(member.getId(), item.getId(), orderCount);
        
        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소 시 상태는 CANCEL");
        assertEquals(10, item.getStockQuantity(), "주문 취소 시 재고가 다시 증가해야 한다.");

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "서울로", "03909"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}