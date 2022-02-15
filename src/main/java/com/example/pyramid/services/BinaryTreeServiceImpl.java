package com.example.pyramid.services;

import com.example.pyramid.model.BinaryTreePerson;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.enums.TransactionType;
import com.example.pyramid.model.enums.bstEnums.PositionInBinaryTree;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.services.api.BinaryTreeService;
import com.example.pyramid.utils.Calculator;
import com.example.pyramid.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.math.RoundingMode.FLOOR;

@Service
@Transactional
public class BinaryTreeServiceImpl extends _BaseService implements BinaryTreeService {

    @Autowired
    BankService bankService;

    @Override
    public void positionInBinaryTree(Person person, BinaryTreePerson parent, PositionInBinaryTree position) {
        Objects.requireNonNull(person , "function parameter person cannot be null");
        Objects.requireNonNull(parent , "function parameter parent cannot be null");
        Objects.requireNonNull(person , "function parameter position cannot be null");

        BinaryTreePerson bstPerson = new BinaryTreePerson();
        bstPerson.setPerson(person);
        bstPerson.setParent(parent);
        bstPerson.setPosition(position);
        em.persist(bstPerson);
    }

    @Override
    public void addAmount(Person person, BigDecimal amount) {
        if (Calculator.isNegative(amount)) throw new RuntimeException("Amount cannot be negative");
        if (Objects.isNull(person)) throw new RuntimeException("Person must exist");

        BinaryTreePerson bstPerson = em.createQuery
                        ("select bstPerson from BinaryTreePerson bstPerson where bstPerson.person =: pPerson"
                                , BinaryTreePerson.class)
                .setParameter("pPerson", person).getSingleResult();

        bstPerson.getMidBox().setValue(bstPerson.getMidBox().getValue().add(amount));

        AtomicReference<PositionInBinaryTree> initialPosition = new AtomicReference<>(bstPerson.getPosition());

        parentChain(bstPerson).forEach(bstParent -> {
            if (initialPosition.get().equals(PositionInBinaryTree.Left))
                bstParent.getLeftBox().setValue(bstParent.getLeftBox().getValue().add(amount));
            else if (initialPosition.get().equals(PositionInBinaryTree.Right))
                bstParent.getRightBox().setValue(bstParent.getRightBox().getValue().add(amount));
            initialPosition.set(bstParent.getPosition());
        });
    }

    @Override
    public void processGroupBonus() {
        BinaryTreePerson companyInBST = em.createQuery(
                        "select bstPerson from BinaryTreePerson bstPerson where bstPerson.id = 1", BinaryTreePerson.class)
                .getSingleResult();

        em.createQuery("select bstPerson from BinaryTreePerson bstPerson", BinaryTreePerson.class)
                .getResultList().parallelStream().forEach(participant -> {

                    //check if tax is paid
                    if (LocalDate.now().isAfter(participant.getPerson().getTaxExpirationDate())) return;

                    //check if condition for min money is completed
                    if (participant.getRightBox().getValue().add(participant.getLeftBox().getValue()).compareTo(Properties.BIG_DECIMAL_10000) < 0)
                        return;

                    List<Person> getAllChildren = em.createQuery("select person from Person person where person.parent =: pParent " +
                                    "and person.registrationDate between : nowDate and : minusOneMonthDate", Person.class)
                            .setParameter("pParent", participant)
                            .setParameter("nowDate", LocalDate.now())
                            .setParameter("minusOneMonthDate", LocalDate.now().minusMonths(1L))
                            .getResultList();

                    //check if children are more than two
                    if (getAllChildren.size() < 2) return;

                    BigDecimal minAmount = participant.getLeftBox().getValue().min(participant.getRightBox().getValue());

                    bankService.transferMoney(
                            companyInBST.getPerson().getAccount(),
                            participant.getPerson().getAccount(),
                            minAmount.multiply(participant.getPerson().getTaxTypePaid().getBonusPercentsInGroupBonus())
                                    .divide(Properties.BIG_DECIMAL_100, FLOOR).setScale(2, FLOOR),
                            TransactionType.Group_Bonus);
                });
    }

    private static Stream<BinaryTreePerson> parentChain(BinaryTreePerson person) {
        return Objects.nonNull(person.getParent())
                ? Stream.concat(Stream.of(person.getParent()), parentChain(person.getParent())) : Stream.empty();
    }
}
