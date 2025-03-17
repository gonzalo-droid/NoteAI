package com.gondroid.taskai.test.repository

import com.gondroid.noteai.data.local.task.RoomTaskLocalDataSource
import com.gondroid.noteai.data.local.task.TaskDao
import com.gondroid.noteai.data.local.task.TaskEntity
import com.gondroid.noteai.test.util.MainDispatcherRule
import com.gondroid.noteai.test.util.MockUtil.mockTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskLocalDataSourceTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var dataSource: RoomTaskLocalDataSource
    private val taskDao: TaskDao = mockk(relaxed = true)

    @Before
    fun setup() {
        dataSource = RoomTaskLocalDataSource(taskDao, coroutineRule.testDispatcher)
    }

    @Test
    fun `tasksFlow should emit transformed tasks`() = runTest {
        val tasksEntities = listOf(
            TaskEntity.fromTask(mockTask(id = "1")),
            TaskEntity.fromTask(mockTask(id = "2", title = "Title", description = "Content")),
            TaskEntity.fromTask(mockTask(id = "3"))
        )
        every { taskDao.getAllTasks() } returns flowOf(tasksEntities)

        val result = dataSource.tasksFlow.first()

        assertEquals(3, result.size)
        assertEquals("Title", result[1].title)
        assertEquals("Content", result[1].description)
    }

    @Test
    fun `addTask should call upsertTask`() = runTest {
        val task = mockTask()

        dataSource.addTask(task)

        coVerify { taskDao.upsertTask(TaskEntity.fromTask(task)) }
    }

    @Test
    fun `removeTask should call deleteTaskById`() = runTest {
        val task = mockTask()
        dataSource.removeTask(task)
        coVerify { taskDao.deleteTaskById(task.id) }
    }

    @Test
    fun `getTaskById should return correct task`() = runTest {
        val taskEntity =
            TaskEntity.fromTask(mockTask(id = "2", title = "Title", description = "Content"))
        coEvery { taskDao.getTaskById("1") } returns taskEntity

        val result = dataSource.getTaskById("1")

        assertEquals("Title", result?.title)
        assertEquals("Content", result?.description)
    }

}