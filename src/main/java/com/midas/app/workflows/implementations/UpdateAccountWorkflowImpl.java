package com.midas.app.workflows.implementations;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.midas.app.services.TemporalServiceImpl;
import com.midas.app.workflows.UpdateAccountWorkflow;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

@WorkflowImpl(taskQueues = UpdateAccountWorkflow.QUEUE_NAME)
public class UpdateAccountWorkflowImpl implements UpdateAccountWorkflow {

  private final AccountActivity accountActivity =
      Workflow.newActivityStub(AccountActivity.class, TemporalServiceImpl.createActivityOptions());

  @Override
  public Account updateAccount(Account details) {
    Account paymentAccount = accountActivity.updatePaymentAccount(details);
    return accountActivity.saveAccount(paymentAccount);
  }
}
