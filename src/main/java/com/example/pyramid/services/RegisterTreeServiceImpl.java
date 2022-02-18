package com.example.pyramid.services;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.PartnerRank;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.enums.TransactionType;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.services.api.RegisterTreeService;
import com.example.pyramid.utils.Calculator;
import com.example.pyramid.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RegisterTreeServiceImpl extends _BaseService implements RegisterTreeService {

    @Autowired
    BankService bankService;

    @Override
    public void registerUser(Person person, Long parentId) {
        Person newPerson = Person.builder()
                .setName(person.getName())
                .setParent(em.find(Person.class, parentId))
                .setRegistrationDate(LocalDate.now())
                .build();

        em.persist(newPerson);
    }

    @Override
    public void createBankAccount(Person person, BigDecimal amount) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setBalance(amount);
        em.persist(newBankAccount);
    }

    @Override
    public void processAllPartnerBonuses() {
        em.createQuery("select persons from Person persons", Person.class)
                .getResultList().forEach(this::processPartnerBonus);
    }


    private void processPartnerBonus(Person person) {

        Objects.requireNonNull(person, "cannot process partner bonus on null person");

        //finding all direct children of person that are minimum rank 3
        List<Person> childrenMinRankThree = em.createQuery(
                        "select children from Person children where children.parent =: pPersonForBonus", Person.class)
                .setParameter("pPersonForBonus", person).getResultList().stream()
                .filter(child -> child.getGroupBonus().compareTo(Properties.BIG_DECIMAL_10000) >= 0)
                .toList();

        //finding rank of person
        PartnerRank rankOfPerson = em.createQuery(
                        "select partnerRank from PartnerRank partnerRank " +
                                "where partnerRank.threshold < :pAmount and partnerRank.count < :pChildrenCount order by partnerRank.threshold desc", PartnerRank.class)
                .setParameter("pAmount", person.getGroupBonus())
                .setParameter("pChildrenCount", childrenMinRankThree.size())
                .setMaxResults(1).getSingleResult();

        //fill list bonusFromEveryChild
        List<BigDecimal> bonusFromEveryChild = new ArrayList<>();
        calculatePartnerBonuses(person, 1, rankOfPerson, bonusFromEveryChild);

        //calculate sum of all partner bonuses from every child
        BigDecimal finalPartnerBonus = bonusFromEveryChild.parallelStream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //process partner bonus amount
        bankService.transferMoney(
                em.find(Person.class, Properties.COMPANY_ID).getAccount(),
                person.getAccount(),
                finalPartnerBonus,
                TransactionType.Partner_Bonus);
    }

    private void calculatePartnerBonuses(Person person, int levelCounter,
                                         PartnerRank rankOfPersonForBonus, List<BigDecimal> amountFromEveryChild) {

        List<Person> children = em.createQuery("select children from Person children where children.parent =: pPerson", Person.class)
                .setParameter("pPerson", person).getResultList();

        //recursion bottom condition
        if (!children.isEmpty() && levelCounter <= 7)
            children.forEach(p -> {

                //check if person has groupBonus
                if (!Calculator.isZero(p.getGroupBonus())) {

                    //getting from group bonus of every child depending on persons rank and children level
                    switch (levelCounter) {
                        case 1: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration1(), p.getGroupBonus()));
                        break;
                        case 2: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration2(), p.getGroupBonus()));
                        break;
                        case 3: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration3(), p.getGroupBonus()));
                        break;
                        case 4: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration4(), p.getGroupBonus()));
                        break;
                        case 5: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration5(), p.getGroupBonus()));
                        break;
                        case 6: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration6(), p.getGroupBonus()));
                        break;
                        case 7: amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration7(), p.getGroupBonus()));
                        break;
                        default: throw new RuntimeException("stana bela");
                    }
                    p.setGroupBonus(BigDecimal.ZERO);
                }

                //recursively call its children
                calculatePartnerBonuses(p, levelCounter + 1, rankOfPersonForBonus, amountFromEveryChild);
            });
    }
}
