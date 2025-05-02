package com.gondroid.noteai.test.database

import com.gondroid.noteai.data.local.task.TaskDao
import com.gondroid.noteai.data.local.task.TaskEntity
import com.gondroid.noteai.test.util.MockUtil.mockTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskDaoTest : LocalDatabase() {

    private lateinit var taskDao: TaskDao

    @Before
    fun init() {
        taskDao = db.taskDao()
    }

    @Test
    fun upsertTaskTest() = runBlocking {
        val mockData = TaskEntity.fromTask(mockTask(id = "1"))
        taskDao.upsertTask(mockData)
        val retrievedData = taskDao.getTaskById("1")
        assertEquals(retrievedData.toString(), mockData.toString())
    }

    @Test
    fun deleteTaskByIdTest() = runBlocking {
        val mockData = TaskEntity.fromTask(mockTask(id = "1"))
        taskDao.upsertTask(mockData)
        taskDao.deleteTaskById("1")
        val retrievedData = taskDao.getTaskById("1")
        assertEquals(retrievedData, null)
    }

    @Test
    fun tasksFlowTest() = runBlocking {
        val data = listOf<TaskEntity>(
            TaskEntity.fromTask(mockTask(id = "1", title = "Task 1")),
            TaskEntity.fromTask(mockTask(id = "2", title = "Task 2"))
        )

        data.forEach { task -> taskDao.upsertTask(task) }

        val tasks = taskDao.getAllTasks().first()

        assertEquals(2, tasks.size)
        assertEquals("Task 1", tasks[0].title)
        assertEquals("Task 2", tasks[1].title)
    }
}
