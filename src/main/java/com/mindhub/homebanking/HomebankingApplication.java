package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder = null;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	public static Card compareAndCreateCard(String cardNumber1, String cardNumber2, String cardNumber3, Client client, CardColor color, CardType type, short cvv, LocalDateTime from, LocalDateTime thru){

		if(!cardNumber1.equals(cardNumber2) && !cardNumber1.equals(cardNumber3) && !cardNumber2.equals(cardNumber3)){

			return new Card(client.getFirstName() + " " + client.getLastName(), color, cardNumber1, type, cvv, from, thru);
		}else{
			Random rand = new Random();
			int num = rand.nextInt(10000);
			int num2 = rand.nextInt(10000);
			int num3 = rand.nextInt(10000);
			int num4 = rand.nextInt(10000);

			from = LocalDateTime.now();
			thru = from.plus(Period.ofYears(5));

			String num1String = String.format("%04d", num);
			String num2String = String.format("%04d", num2);
			String num3String = String.format("%04d", num3);
			String num4String = String.format("%04d", num4);

			cardNumber1 = num1String + "-" + num2String + "-" + num3String + "-" + num4String;

			return new Card(client.getFirstName() + " " + client.getLastName(), color, cardNumber1, type, cvv, from, thru);
		}
	}

	public static String generateNumberCard(){
		Random rand = new Random();
		int num = rand.nextInt(10000);
		int num2 = rand.nextInt(10000);
		int num3 = rand.nextInt(10000);
		int num4 = rand.nextInt(10000);

		String num1String = String.format("%04d", num);
		String num2String = String.format("%04d", num2);
		String num3String = String.format("%04d", num3);
		String num4String = String.format("%04d", num4);

		return  num1String + "-" + num2String + "-" + num3String + "-" + num4String;
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRerpository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			Client client1 = new Client("Lalo", "Landa", "test@test.com", passwordEncoder.encode("1234"));
			Client client2 = new Client("Milhouse", "Van Houten", "milhouse@springfield.com",passwordEncoder.encode("asd"));
			Client admin = new Client("Admin", "Admin", "admin@admin.com",passwordEncoder.encode("asd"));

			Random rand = new Random();

			int cvv = rand.nextInt(1000);
			String cvvStr = String.format("%03d", cvv);
			short ccvNumber = Short.parseShort(cvvStr);

			int cvv2 = rand.nextInt(1000);
			String cvvStr2 = String.format("%03d", cvv2);
			short ccvNumber2 = Short.parseShort(cvvStr2);

			int cvv3 = rand.nextInt(1000);
			String cvvStr3 = String.format("%03d", cvv3);
			short ccvNumber3 = Short.parseShort(cvvStr3);

			LocalDateTime from = LocalDateTime.now();
			LocalDateTime thru = from.plus(Period.ofYears(5));

			LocalDateTime expiredCardFrom = LocalDateTime.now().minusYears(6);
			LocalDateTime expiredCardThru = expiredCardFrom.plus(Period.ofYears(5));

			String cardNumber1 = generateNumberCard();
			String cardNumber2 = generateNumberCard();
			String cardNumber3 = generateNumberCard();

			Card card1 = compareAndCreateCard(cardNumber1, cardNumber2, cardNumber3, client1, CardColor.GOLD, CardType.DEBIT, ccvNumber, expiredCardFrom, expiredCardThru);
			Card card2 = compareAndCreateCard(cardNumber2, cardNumber1, cardNumber3, client1, CardColor.TITANIUM, CardType.CREDIT, ccvNumber2, from, thru);
			Card card3 = compareAndCreateCard(cardNumber3, cardNumber2, cardNumber1, client2, CardColor.SILVER, CardType.CREDIT, ccvNumber3, from, thru);

			Loan loan1 = new Loan("mortgage", 500000, 0.20);
			Loan loan2 = new Loan("personal", 100000,0.30);
			Loan loan3 = new Loan("automotive", 300000, 0.15);

			List<Integer> mortgage = List.of(12,24,36,48,60);
			List<Integer> personal = List.of(6,12,24);
			List<Integer> automotive = List.of(6,12,24,36);

			loan1.setPayments(mortgage);
			loan2.setPayments(personal);
			loan3.setPayments(automotive);

			ClientLoan loanTest = new ClientLoan(loan1, 60, 400.000, client1);
			ClientLoan loanTest2 = new ClientLoan(loan2, 12, 50.000, client1);
			ClientLoan loanTest3 = new ClientLoan(loan2, 24, 100.000, client2);
			ClientLoan loanTest4 = new ClientLoan(loan3, 36, 200.000, client2);

			client1.addLoan(loanTest);
			client1.addLoan(loanTest2);

			client2.addLoan(loanTest3);
			client2.addLoan(loanTest4);

			Account VIN001 = new Account(LocalDateTime.now(), 5000.0, "VIN001", false, AccountType.CHECKING);
			Account VIN002 = new Account(LocalDateTime.now().plusDays(1), 7500.0, "VIN002", false, AccountType.SAVINGS);

			Account VIN003 = new Account(LocalDateTime.now(), 2000.0, "VIN003", false, AccountType.SAVINGS);
			Account VIN004 = new Account(LocalDateTime.now(), 10000.0, "VIN004", false, AccountType.CHECKING);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 5000.0, "Credit transaction made for $5,000", LocalDateTime.now(), 5000.0, false);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 10000, "Debit transaction made for $10,000", LocalDateTime.now(),10000.0, false);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 2000, "Debit transaction made for $10,000 $2,000", LocalDateTime.now(), 1000.0, false);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 6000, "Debit transaction made for $10,000 $6,000", LocalDateTime.now(), 6000.0, false);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 15000, "Credit transaction made for $15,000", LocalDateTime.now(), 15000.0, false);
			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 5600, "Credit transaction made for $5,600", LocalDateTime.now(), 5600.0, false);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 40050, "Credit transaction made for $40,050", LocalDateTime.now(), 40050.0, false);
			Transaction transaction8 = new Transaction(TransactionType.CREDIT, 9000, "Credit transaction made for $9,000", LocalDateTime.now(), 9000.0, false);
			Transaction transaction9 = new Transaction(TransactionType.CREDIT, 120000, "Credit transaction made for $120,000", LocalDateTime.now(), 120000.0, false);
			Transaction transaction10 = new Transaction(TransactionType.DEBIT, 4000, "Debit transaction made for $10,000 $4,000", LocalDateTime.now(), 4000.0, false);
			Transaction transaction11 = new Transaction(TransactionType.DEBIT, 50200, "Debit transaction made for $10,000 $50,200", LocalDateTime.now(), 50200.0, false);
			Transaction transaction12 = new Transaction(TransactionType.DEBIT, 7000, "Debit transaction made for $10,000 $7,000", LocalDateTime.now(), 7000.0, false);
			Transaction transaction13 = new Transaction(TransactionType.DEBIT, 12220, "Debit transaction made for $10,000 $12,220", LocalDateTime.now(), 12220.0, false);
			Transaction transaction14 = new Transaction(TransactionType.DEBIT, 1000, "Debit transaction made for $10,000 $1,000", LocalDateTime.now(), 1000.0, false);
			Transaction transaction15 = new Transaction(TransactionType.DEBIT, 20000, "Debit transaction made for $10,000 $20,000", LocalDateTime.now(), 20000.0, false);

			VIN001.addTransaction(transaction1);
			VIN001.addTransaction(transaction2);
			VIN001.addTransaction(transaction3);
			VIN001.addTransaction(transaction4);
			VIN001.addTransaction(transaction5);
			VIN001.addTransaction(transaction6);
			VIN001.addTransaction(transaction7);
			VIN002.addTransaction(transaction8);
			VIN002.addTransaction(transaction9);
			VIN002.addTransaction(transaction10);
			VIN002.addTransaction(transaction11);
			VIN003.addTransaction(transaction12);
			VIN003.addTransaction(transaction13);
			VIN004.addTransaction(transaction14);
			VIN004.addTransaction(transaction15);

			client1.addAccount(VIN001);
			client1.addAccount(VIN002);
			client2.addAccount(VIN003);
			client2.addAccount(VIN004);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(admin);

			loanRerpository.save(loan1);
			loanRerpository.save(loan2);
			loanRerpository.save(loan3);

			accountRepository.save(VIN001);
			accountRepository.save(VIN002);
			accountRepository.save(VIN003);
			accountRepository.save(VIN004);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);
			transactionRepository.save(transaction13);
			transactionRepository.save(transaction14);
			transactionRepository.save(transaction15);

			clientLoanRepository.save(loanTest);
			clientLoanRepository.save(loanTest2);
			clientLoanRepository.save(loanTest3);
			clientLoanRepository.save(loanTest4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		};

	}
}
