package com.midas.app.services;

import com.midas.app.exceptions.ResourceAlreadyExistsException;
import com.midas.app.exceptions.ResourceNotFoundException;
import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.shared.TemporalService;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.UpdateAccountWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  private final WorkflowClient workflowClient;

  private final AccountRepository accountRepository;

  private final TemporalService temporalService;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    preCreateAccount(details);
    var createAccountWorkflow =
        this.temporalService.createWorkflowStub(
            CreateAccountWorkflow.class, CreateAccountWorkflow.QUEUE_NAME, details.getEmail());

    logger.info("initiating workflow to create account for email: {}", details.getEmail());
    return createAccountWorkflow.createAccount(details);
  }

  private void preCreateAccount(Account details) {
    details.setProviderType(ProviderType.STRIPE);
  }

  /**
   * updateAccount updates the account in the system or provider.
   *
   * @param details is the details of the account to be updated.
   * @return Account
   */
  @Override
  public Account updateAccount(UUID id, Account details) {

    Account finalAccount = getFinalAccountDetails(id, details);
    var updateAccountWorkflow =
        this.temporalService.createWorkflowStub(
            UpdateAccountWorkflow.class, UpdateAccountWorkflow.QUEUE_NAME, finalAccount.getEmail());

    logger.info("initiating workflow to update account for email: {}", finalAccount.getEmail());
    return updateAccountWorkflow.updateAccount(finalAccount);
  }

  private Account getFinalAccountDetails(UUID id, Account details) {
    Account account = getAccountById(id);
    account.setFirstName(details.getFirstName());
    account.setLastName(details.getLastName());
    account.setEmail(details.getEmail());
    return account;
  }

  /**
   * updateAccount saves a new account in the system.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account saveAccount(Account details) {
    return accountRepository.save(details);
  }

  /**
   * deleteAccount deletes the account in the system.
   *
   * @param id is the id of the account to be deleted.
   */
  @Override
  public void deleteAccount(UUID id) {
    accountRepository.deleteById(id);
  }

  /**
   * deleteAccount validates the account in the system.
   *
   * @param details is the details of the account to be validated.
   */
  public void checkAndValidateAccount(Account details) {
    String email = details.getEmail();
    Example<Account> studentExample = Example.of(Account.builder().email(email).build());
    if (accountRepository.exists(studentExample)) {
      throw new ResourceAlreadyExistsException();
    }
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account getAccountById(UUID id) {
    return accountRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
  }
}
