package com.kantartraining.banksystem.service;

import com.kantartraining.banksystem.dto.AccountDTO;
import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.entity.Account;
import com.kantartraining.banksystem.entity.Customer;
import com.kantartraining.banksystem.entity.Transaction;
import com.kantartraining.banksystem.exception.InsufficientBalanceException;
import com.kantartraining.banksystem.exception.ResourceNotFoundException;
import com.kantartraining.banksystem.repository.AccountRepository;
import com.kantartraining.banksystem.repository.CustomerRepository;
import com.kantartraining.banksystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;

    @Transactional
    public AccountDTO createAccount(Long customerId, AccountDTO request) {
        // Get customer details
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        // Generate account number manually (thread-safe)
        Long accountNo = generateNextAccountNumber();

        // Create Account entity
        Account account = new Account();
        account.setAccountNo(accountNo);  // Set manually generated number
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance() != null ? request.getBalance() : 0.0);

        String accountHolderName = (request.getAccountHolderName() != null && !request.getAccountHolderName().trim().isEmpty())
                ? request.getAccountHolderName()
                : customer.getFirstName() + " " + customer.getLastName();

        account.setAccountHolderName(accountHolderName);
        account.setCustomer(customer);

        // Save entity
        Account savedAccount = accountRepository.save(account);

        return convertToDTO(savedAccount);
    }

    private synchronized Long generateNextAccountNumber() {
        try {
            Long maxAccountNo = accountRepository.findMaxAccountNumber();
            return (maxAccountNo != null ? maxAccountNo : 100000L) + 1L;
        } catch (Exception e) {
            // Fallback to timestamp-based generation if query fails
            return System.currentTimeMillis() % 1000000000L;
        }
    }

    public AccountDTO getAccount(Long accountNo) {
        log.info("Fetching account: {}", accountNo);
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNo));
        return convertToDTO(account);
    }

    public double getBalance(Long accountNo) {
        log.info("Fetching balance for account: {}", accountNo);
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNo));
        return account.getBalance();
    }

    @Transactional
    public AccountDTO deposit(Long accountNo, double amount, LocalDate date) {
        log.info("Processing deposit of {} to account: {}", amount, accountNo);

        // Use a fresh database query within the transaction
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNo));

        if (amount <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        // Calculate new balance
        double newBalance = account.getBalance() + amount;

        // Use direct update query to avoid detached entity issues
        accountRepository.updateBalance(accountNo, newBalance);

        // Refresh the account entity
        Account updatedAccount = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found after update: " + accountNo));

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionType("Deposit");
        transaction.setAmount(amount);
        transaction.setAccount(updatedAccount);
        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);

        log.info("Deposit successful. New balance: {} for account: {}", updatedAccount.getBalance(), accountNo);
        return convertToDTO(updatedAccount);
    }

    @Transactional
    public AccountDTO withdraw(Long accountNo, double amount, LocalDate date) {
        log.info("Processing withdrawal of {} from account: {}", amount, accountNo);

        // Use a fresh database query within the transaction
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNo));

        if (amount <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        // Calculate new balance
        double newBalance = account.getBalance() - amount;

        // Use direct update query to avoid detached entity issues
        accountRepository.updateBalance(accountNo, newBalance);

        // Refresh the account entity
        Account updatedAccount = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found after update: " + accountNo));

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionType("Withdrawal");
        transaction.setAmount(amount);
        transaction.setAccount(updatedAccount);
        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);

        log.info("Withdrawal successful. New balance: {} for account: {}", updatedAccount.getBalance(), accountNo);
        return convertToDTO(updatedAccount);
    }

    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        log.info("Fetching accounts for customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private AccountDTO convertToDTO(Account account) {
        List<Long> transactionIds = account.getTransaction()
                .stream()
                .map(Transaction::getTransactionId)
                .toList();

        return new AccountDTO(
                account.getAccountNo(),  // Add this - the account number
                account.getAccountType(),
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),
                account.getCustomer().getId(),
                account.getBalance(),
                transactionIds
        );
    }
}