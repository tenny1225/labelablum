package com.lenovo.common.manager;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * 异步任务管理类
 */

public class AsyncExecutorQueueManager {
    /**
     * 任务完成监听器
     */
    public interface TaskCompletedListener {
        void onCompleted();
    }

    /**
     * 任务类
     *
     */
    public static abstract class SimpleTask extends AsyncTask<Void, Void, Void> {
        private TaskTrigger taskTrigger;

        public void setTaskTrigger(TaskTrigger taskTrigger) {
            this.taskTrigger = taskTrigger;
        }



        @Override
        protected void onPostExecute(Void t) {
            onCompleted();
            if (taskTrigger != null) {
                taskTrigger.removeTask(this);
            }

            super.onPostExecute(t);

        }

        protected  void onCompleted(){};
    }

    /**
     * 任务触发器类
     */
    public static class TaskTrigger {

        private List<SimpleTask> taskList = new ArrayList<>();
        private TaskCompletedListener taskCompletedListener;

        public TaskTrigger(List<SimpleTask> taskList) {
            this.taskList.addAll(taskList);
        }

        public void setTaskCompletedListener(TaskCompletedListener taskCompletedListener) {
            this.taskCompletedListener = taskCompletedListener;
        }

        public void removeTask(SimpleTask task) {
            taskList.remove(task);
            if (taskList.size() == 0) {
                if (taskCompletedListener != null) {
                    taskCompletedListener.onCompleted();
                }
            }
        }
    }

    private List<SimpleTask> taskList;//异步任务列表


    public AsyncExecutorQueueManager() {
        this.taskList = new ArrayList<>();

    }

    /**
     * 添加异步任务
     * @param task
     */
    public void addTask(SimpleTask task) {
        taskList.add(task);

    }

    /**
     * 开始所有异步任务，最后一个异步任务是所有任务的终点
     */
    public void start() {
        if (taskList.size() == 1) {
            taskList.get(0).executeOnExecutor(ExecutorServiceFactory.createAlgorithmExecutor());
            return;
        }
        TaskTrigger taskTrigger = new TaskTrigger(taskList.subList(0, taskList.size() - 1));
        taskTrigger.setTaskCompletedListener(new TaskCompletedListener() {
            @Override
            public void onCompleted() {
                taskList.get(taskList.size() - 1).executeOnExecutor(ExecutorServiceFactory.createAlgorithmExecutor());
            }
        });
        for (int i = 0; i < taskList.size() - 1; i++) {
            SimpleTask task = taskList.get(i);
            task.setTaskTrigger(taskTrigger);
            task.executeOnExecutor(ExecutorServiceFactory.createAlgorithmExecutor());
        }


    }
}
