package br.com.mwdesenvolvimento.mylibrary.server;

/**
 * Created by mindware on 10/03/15.
 */
public interface TaskListener {

  TaskListener NULL = new TaskListener() {
    @Override
    public void act(String result, Task task, Exception e) {}
  };
  /**
   * this method will be called from RequestTask after his job getting done
   * @param result
   * @param task
   */
  void act(String result, Task task, Exception e);
}
