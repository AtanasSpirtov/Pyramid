package com.example.pyramid.services;

import com.example.pyramid.model.BinaryTreePerson;
import com.example.pyramid.model.BonusReport;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.example.pyramid.model.enums.bstEnums.BonusStatus.*;

@Service
@Transactional
public class BinaryTreeServiceImpl extends _BaseService implements BinaryTreeService {

    @Autowired
    BankService bankService;

    private static final int MINIMAL_PEOPLE_REGISTERED_THIS_MONTH = 2;

    @Override
    public void positionInBinaryTree(Person person, BinaryTreePerson parent, PositionInBinaryTree position, BinaryTreePerson registrator) {
        Objects.requireNonNull(person, "function parameter person cannot be null");
        Objects.requireNonNull(parent, "function parameter parent cannot be null");
        Objects.requireNonNull(person, "function parameter position cannot be null");
        if (!checkIfInRegistratorTree(registrator))
            throw new RuntimeException("not in registrator tree");

        BinaryTreePerson bstPerson = new BinaryTreePerson();
        bstPerson.setPerson(person);
        bstPerson.setParent(parent);
        bstPerson.setPosition(position);
        em.persist(bstPerson);
    }

    @Override
    public void addAmount(Person person, BigDecimal amount) {
        if (Calculator.isNegative(amount))
            throw new RuntimeException("Amount cannot be negative");

        BinaryTreePerson bstPerson = findBinaryPerson(person);

        bstPerson.setMidBox(bstPerson.getMidBox().add(amount));
        List<BinaryTreePerson> parentChainResult = parentChain(bstPerson).toList();

        PositionInBinaryTree initialPosition = bstPerson.getPosition();

        for (BinaryTreePerson bstParent : parentChainResult) {
            if (initialPosition == PositionInBinaryTree.Left) {
                bstParent.setLeftBox(bstParent.getLeftBox().add(amount));
            } else if (initialPosition == PositionInBinaryTree.Right) {
                bstParent.setRightBox(bstParent.getRightBox().add(amount));
            }
            initialPosition = bstParent.getPosition();
        }
    }

    @Override
    public void processGroupBonus() {
        BinaryTreePerson companyInBST = em.createNamedQuery("BinaryTreePerson.findCompany", BinaryTreePerson.class).getSingleResult();

        em.createNamedQuery("BinaryTreePerson.findAllBinaryTreePersons", BinaryTreePerson.class).getResultList()
                .stream()
                .filter(BinaryTreeServiceImpl::isActive)
                .filter(this::isQualifiedForGroupBonus)
                .forEach(participant -> {
                    BigDecimal minAmount = getMinValueFromBoxes(participant);
                    participant.getPerson().setGroupBonus(
                            Calculator.findPercent(getPerson(participant).getTaxTypePaid().getBonusPercentsInGroupBonus(), minAmount));

                    bankService.transferMoney(companyInBST.getPerson().getAccount(), getPerson(participant).getAccount(),
                            getPerson(participant).getGroupBonus(), TransactionType.Group_Bonus);

                    writeGroupBonusReport(participant);
                });
    }

    private Person getPerson(BinaryTreePerson p) {
        return p.getPerson();
    }

    private BinaryTreePerson findBinaryPerson(Person person) {
        Objects.requireNonNull(person, "Person must exist");
        return em.createNamedQuery("BinaryTreePerson.findBinaryTreePerson", BinaryTreePerson.class).setParameter("pPerson", person).getSingleResult();
    }

    private boolean isQualifiedForGroupBonus(BinaryTreePerson p) {
        boolean eligible = false;

        // check if condition for minimal group bonus received is completed
        BigDecimal minAmount = getMinValueFromBoxes(p);

        if (Calculator.greaterThan(minAmount, Properties.BIG_DECIMAL_10000)) {

            List<BinaryTreePerson> registeredPeople = getRegistratedPeople(p);
            eligible = registeredPeople.size() >= MINIMAL_PEOPLE_REGISTERED_THIS_MONTH && checkRegistrationPeople(registeredPeople);
        }

        return eligible;
    }

    private static boolean isActive(BinaryTreePerson p) {
        return LocalDate.now().isAfter(p.getPerson().getTaxExpirationDate());
    }

    private List<BinaryTreePerson> getRegistratedPeople(BinaryTreePerson participant) {
        LocalDate taxPaymentTime = LocalDate.now();
        return em.createNamedQuery("BinaryTreePerson.selectPeopleRegistratedLastMonth", BinaryTreePerson.class)
                .setParameter("pRegistrator", participant).setParameter("nowDate", taxPaymentTime)
                .setParameter("minusOneMonthDate", taxPaymentTime.minusMonths(1L)).getResultList();
    }

    private static Stream<BinaryTreePerson> parentChain(BinaryTreePerson person) {
        return Objects.nonNull(person.getParent()) ? Stream.concat(Stream.of(person.getParent()), parentChain(person.getParent())) : Stream.empty();
    }

    private static boolean checkIfInRegistratorTree(BinaryTreePerson person) {
        return parentChain(person).anyMatch(parent -> parent.equals(person));
    }

    // TODO Maybe create BonusReportService
    private static void writeGroupBonusReport(BinaryTreePerson p) {
        BonusReport bonusReport = p.getBonusReport();
        bonusReport.setProcessTime(LocalDateTime.now());
        bonusReport.setStatus(PratnerBonus);
    }

    private static boolean checkRegistrationPeople(List<BinaryTreePerson> registrationPeople) {
        boolean leftFound = false;
        boolean rightFound = false;
        for (BinaryTreePerson registrationPerson : registrationPeople) {
            if (registrationPerson.getPosition() == PositionInBinaryTree.Left) {
                leftFound = true;
            } else {
                rightFound = true;
            }
            if(leftFound && rightFound)
            {
                return true;
            }
        }
        return false;
    }

    private static BigDecimal getMinValueFromBoxes(BinaryTreePerson p) {
        return p.getLeftBox().min(p.getRightBox());
    }

}