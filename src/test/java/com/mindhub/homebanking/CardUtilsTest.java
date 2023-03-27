package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;



import com.mindhub.homebanking.controllers.AccountController;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

//@SpringBootTest
//public class CardUtilsTest {
//
//    @Test
//    public void cardNumberIsCreated(){
//        String cardNumber = CardUtils.getCardNumber();
//        assertThat(cardNumber,is(not(emptyOrNullString())));
//    }
//
//    @Test
//    public void cardHasAllNumbers(){
//        String cardNumber = CardUtils.getCardNumber();
//        assertThat(cardNumber, containsString("-"));
//    }
//
//    @Test
//    public void cardCvvIsNotEmpty(){
//        short cvvNumber = CardUtils.getCcvNumber();
//        assertThat(cvvNumber, is(not(equalTo((short) 0))));
//    }
//
//    @Test
//    public void cardHasThreeNumbers(){
//        short cardCvv = CardUtils.getCcvNumber();
//        Assertions.assertThat(cardCvv).isBetween((short) 0, (short) 999);
//    }
//
//
//}
