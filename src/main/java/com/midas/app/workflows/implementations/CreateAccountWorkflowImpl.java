package com.midas.app.workflows.implementations;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.midas.app.shared.TemporalServiceImpl;
import com.midas.app.workflows.CreateAccountWorkflow;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

@WorkflowImpl(taskQueues = CreateAccountWorkflow.QUEUE_NAME)
public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  private final AccountActivity accountActivity =
      Workflow.newActivityStub(AccountActivity.class, TemporalServiceImpl.createActivityOptions());

  @Override
  public Account createAccount(Account details) {
    Account account = accountActivity.saveAccount(details);
    Account paymentAccount = accountActivity.createPaymentAccount(account);
    return accountActivity.saveAccount(paymentAccount);
  }
}
