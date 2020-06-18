package acerezo.android.viewmodeltest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is` as iz
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import utils.observeForTesting

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MyViewModelTest {

    @ExperimentalCoroutinesApi
    var coroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewModel = MyViewModel(coroutineDispatcher, coroutineDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        coroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test initial state`() {
        viewModel.uiState.observeForTesting { observer ->
            assertThat(viewModel.uiState.value, iz(nullValue()))
            assertThat(observer.values.size, iz(0))
        }
    }

    @Test
    fun `test loading state`() {
        viewModel.uiState.observeForTesting { observer ->
            assertThat(viewModel.uiState.value, iz(nullValue()))
            assertThat(observer.values.size, iz(0))

            coroutineDispatcher.pauseDispatcher()
            viewModel.fetchData()

            assertThat(viewModel.uiState.value is Loading, iz(true))
            assertThat(observer.values.size, iz(1))
            coroutineDispatcher.resumeDispatcher()
        }
    }

    @Test
    fun `test successes`() = coroutineDispatcher.runBlockingTest {
        viewModel.uiState.observeForTesting { observer ->
            assertThat(viewModel.uiState.value, iz(nullValue()))
            assertThat(observer.values.size, iz(0))

            coroutineDispatcher.pauseDispatcher()
            viewModel.fetchData()
            coroutineDispatcher.advanceTimeBy(1_000)
            assertThat(observer.values.size, iz(2))

            assertThat(viewModel.uiState.value is Success, iz(true))
            assertThat(observer.values[0] is Loading, iz(true))
            assertThat(observer.values[1] is Success, iz(true))
        }
    }

}
