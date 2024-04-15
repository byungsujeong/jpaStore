package jpapaper.jpastore.service;

import jpapaper.jpastore.domain.Item.Book;
import jpapaper.jpastore.domain.Item.Item;
import jpapaper.jpastore.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Book updateBook(Long itemId, String name, int price, int stockQuantity, String author, String isbn) {
        Book findBook = itemRepository.findBook(itemId);
        findBook.setName(name);
        findBook.setPrice(price);
        findBook.setStockQuantity(stockQuantity);
        findBook.setAuthor(author);
        findBook.setIsbn(isbn);
        return findBook;
    }

    public List<Item> findItem() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
