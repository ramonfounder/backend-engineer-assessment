package com.midas.app.shared;

import io.temporal.client.WorkflowOptions;
import javax.annotation.Nullable;

public interface TemporalService {
  /** startWorker initialize temporal */
  public void initialize();

  /**
   * createWorkflowStub create new workflow with given workflow interface and workflow options
   *
   * @param workflowInterface is the interface of your workflow
   * @param workflowOptions is for define your custom workflow options\
   * @return T
   */
  public <T> T createWorkflowStub(
      Class<T> workflowInterface, @Nullable WorkflowOptions workflowOptions);

  /**
   * createWorkflowStub create new workflow with given workflow interface and seperated option
   * variables
   *
   * @param workflowInterface is the interface of your workflow
   * @param taskQueue is the queue name
   * @param workflowId is the custom workflowId
   * @return T
   */
  public <T> T createWorkflowStub(
      Class<T> workflowInterface, String taskQueue, @Nullable String workflowId);

  /**
   * findWorkflowStubById find workflow existed instance by workflowId
   *
   * @param workflowInterface is the interface of your workflow
   * @param workflowId is the existed workflowId
   * @return T
   */
  public <T> T findWorkflowStubById(Class<T> workflowInterface, String workflowId);
}
