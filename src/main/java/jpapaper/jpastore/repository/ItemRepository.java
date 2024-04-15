package jpapaper.jpastore.repository;

import jakarta.persistence.EntityManager;
import jpapaper.jpastore.domain.Item.Book;
import jpapaper.jpastore.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        em.persist(item);
        /*if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }*/
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public Book findBook(Long id) {
        return em.find(Book.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
