package com.example.pyramid.services;

import com.example.pyramid.model.BinaryTreePerson;
import com.example.pyramid.model.BonusReport;
import com.example.pyramid.model.PartnerRank;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.enums.TransactionType;
import com.example.pyramid.model.enums.bstEnums.BonusStatus;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.services.api.PartnerService;
import com.example.pyramid.utils.Calculator;
import com.example.pyramid.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.pyramid.model.enums.bstEnums.BonusStatus.*;

public class PartnerServiceImpl extends _BaseService implements PartnerService {
    @Autowired
    BankService bankService;

    private static final int STARTING_LEVEL = 1;

    @Override
    public void processAllPartnerBonuses() {
        em.createNamedQuery("Person.getAllPeople" , Person.class)
                .getResultList().forEach(this::processPartnerBonus);
    }


    private void processPartnerBonus(Person person) {

        Objects.requireNonNull(person, "cannot process partner bonus on null person");

        //finding all direct children of person that are minimum rank 3
        List<Person> childrenMinRankThree = em.createNamedQuery("People.getAllDirectChildren", Person.class)
                .setParameter("pPersonForBonus", person).getResultList().stream()
                .filter(child -> child.getGroupBonus().compareTo(Properties.BIG_DECIMAL_10000) >= 0)
                .toList();

        //finding rank of person
        PartnerRank rankOfPerson = em.createNamedQuery("PartnerRank.FindPartnerRankForPerson", PartnerRank.class)
                .setParameter("pAmount", person.getGroupBonus())
                .setParameter("pChildrenCount", childrenMinRankThree.size())
                .setMaxResults(1).getSingleResult();

        //fill list bonusFromEveryChild
        List<BigDecimal> bonusFromEveryChild = new ArrayList<>();
        calculatePartnerBonuses(person, STARTING_LEVEL, rankOfPerson, bonusFromEveryChild);

        //calculate sum of all partner bonuses from every child
        BigDecimal finalPartnerBonus = bonusFromEveryChild.parallelStream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //process partner bonus amount
        bankService.transferMoney(
                em.find(Person.class, Properties.COMPANY_ID).getAccount(),
                person.getAccount(),
                finalPartnerBonus,
                TransactionType.Partner_Bonus);

        writeReportForPartnerBonus(person);
    }

    private void calculatePartnerBonuses(Person person, int levelCounter,
                                         PartnerRank rankOfPersonForBonus, List<BigDecimal> amountFromEveryChild) {

        List<Person> children = em.createNamedQuery("People.getAllDirectChildren", Person.class)
                .setParameter("pPersonForBonus", person).getResultList();

        //recursion bottom condition
        if (!children.isEmpty() && levelCounter <= 7)
            children.forEach(p -> {

                //check if person has groupBonus
                if (checkIfTakenGroupBonus(p)) {

                    //getting from group bonus of every child depending on persons rank and children level
                    switch (levelCounter) {
                        case 1 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration1(), p.getGroupBonus()));
                        case 2 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration2(), p.getGroupBonus()));
                        case 3 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration3(), p.getGroupBonus()));
                        case 4 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration4(), p.getGroupBonus()));
                        case 5 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration5(), p.getGroupBonus()));
                        case 6 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration6(), p.getGroupBonus()));
                        case 7 -> amountFromEveryChild.add(Calculator.findPercent(rankOfPersonForBonus.getGeneration7(), p.getGroupBonus()));
                        default -> throw new RuntimeException("stana bela");
                    }
                    p.setGroupBonus(BigDecimal.ZERO);
                }

                //recursively call its children
                calculatePartnerBonuses(p, levelCounter + 1, rankOfPersonForBonus, amountFromEveryChild);
            });
    }
    private boolean checkIfTakenGroupBonus(Person p){
        return  em.createNamedQuery("BinaryTreePerson.findBinaryTreePerson", BinaryTreePerson.class).setParameter("pPerson", p)
                .getSingleResult().getBonusReport().getStatus() == GroupBonus;
    }
    private void writeReportForPartnerBonus(Person p) {
        BonusReport bonusReport = em.createNamedQuery("BinaryTreePerson.findBinaryTreePerson", BinaryTreePerson.class)
                .setParameter("pPerson", p)
                .getSingleResult().getBonusReport();
        bonusReport.setProcessTime(LocalDateTime.now());
        bonusReport.setStatus(BonusStatus.PratnerBonus);
    }
}
