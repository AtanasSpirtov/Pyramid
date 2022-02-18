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

        bstPerson.setMidBox(bstPerson.getMidBox().add(amount));

        AtomicReference<PositionInBinaryTree> initialPosition = new AtomicReference<>(bstPerson.getPosition());

        parentChain(bstPerson).forEach(bstParent -> {
            if (initialPosition.get().equals(PositionInBinaryTree.Left))
                bstParent.setLeftBox(bstParent.getLeftBox().add(amount));
            else if (initialPosition.get().equals(PositionInBinaryTree.Right))
                bstParent.setRightBox(bstParent.getRightBox().add(amount));
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
                    if (Calculator.addToFirst(participant.getLeftBox() , participant.getRightBox()).compareTo(Properties.BIG_DECIMAL_10000) < 0)
                        return;

                    List<BinaryTreePerson> registeredPeople = em.createQuery(
                                    "select bstPerson from BinaryTreePerson bstPerson where bstPerson.registrant =: pRegistrant " +
                                            "and bstPerson.person.registrationDate between : nowDate and : minusOneMonthDate",
                                    BinaryTreePerson.class)
                            .setParameter("pRegistrant", participant)
                            .setParameter("nowDate", LocalDate.now())
                            .setParameter("minusOneMonthDate", LocalDate.now().minusMonths(1L)).getResultList();

                    //check if children are more than two
                    if (registeredPeople.size() < 2) return;

                    if ((checkRegistrationPeople(registeredPeople, PositionInBinaryTree.Left)
                            || checkRegistrationPeople(registeredPeople, PositionInBinaryTree.Right)))
                        return;

                    BigDecimal minAmount = participant.getLeftBox().min(participant.getRightBox());

                    participant.getPerson().setGroupBonus(
                            Calculator.findPercent(participant.getPerson().getTaxTypePaid().getBonusPercentsInGroupBonus() , minAmount));

                    bankService.transferMoney(
                            companyInBST.getPerson().getAccount(),
                            participant.getPerson().getAccount(),
                            participant.getPerson().getGroupBonus(),
                            TransactionType.Group_Bonus);
                });
    }

    private static Stream<BinaryTreePerson> parentChain(BinaryTreePerson person) {
        return Objects.nonNull(person.getParent())
                ? Stream.concat(Stream.of(person.getParent()), parentChain(person.getParent())) : Stream.empty();
    }
    private static boolean checkRegistrationPeople(List<BinaryTreePerson> registrationPeople, PositionInBinaryTree position) {
        return registrationPeople.parallelStream().noneMatch(c -> c.getPosition().equals(position));
    }
}
