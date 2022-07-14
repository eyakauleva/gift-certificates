package com.epam.esm.repos;

import com.epam.esm.consts.MessagesKeys;
import com.epam.esm.consts.NamedQueriesKeys;
import com.epam.esm.entity.Order;
import exception.ResourceNotFoundException;
import com.epam.esm.repos.metadata.TableField;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

@Repository
@ComponentScan("com.epam.esm")
public class OrderRepository {
  private static final Logger logger = Logger.getLogger(CertificateRepository.class);
  private final EntityManager entityManager;
  private final ResourceBundleMessageSource messageSource;

  @Autowired
  public OrderRepository(EntityManager entityManager, ResourceBundleMessageSource messageSource) {
    this.entityManager = entityManager;
    this.messageSource = messageSource;
  }

  /**
   * Creates new order <br>
   *
   * @param order order to create
   * @return created order id
   */
  public Long create(Order order) {
    entityManager.getTransaction().begin();
    entityManager.merge(order);
    entityManager.getTransaction().commit();
    return order.getId();
  }

  /**
   * Searches for order with provided id <br>
   * If order with provided id does not exist, {@code ResourceNotFoundException} is thrown
   *
   * @param id id of the order to search for
   * @return founded order
   */
  public Order findById(Long id) {
    Order order = entityManager.find(Order.class, id);
    if (Objects.nonNull(order)) return order;
    else throw getOrderIdNotExistException(null, id);
  }

  /**
   * Searches for all orders that belong to the user with provided id <br>
   * *
   *
   * @param id id of the user whose orders to search for
   * @return list of founded orders
   */
  public List<Order> findAllUserOrders(Long id) {
    try {
      return entityManager
          .createNamedQuery(NamedQueriesKeys.ORDER_FIND_BY_USER, Order.class)
          .setParameter(TableField.ID, id)
          .getResultList();
    } catch (NoResultException e) {
      throw getOrderIdNotExistException(e, id);
    }
  }

  private ResourceNotFoundException getOrderIdNotExistException(NoResultException e, Long id) {
    logger.error("Order {id ='" + id + "'} does not exist");
    return new ResourceNotFoundException(
        messageSource.getMessage(
            MessagesKeys.ORDER_ID_NOT_EXIST, new Object[] {id}, LocaleContextHolder.getLocale()),
        e);
  }
}
