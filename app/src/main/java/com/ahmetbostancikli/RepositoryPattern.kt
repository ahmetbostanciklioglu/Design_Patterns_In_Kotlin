package com.ahmetbostancikli

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

//Data Class
data class Data(val id: Int, val content: String)

// Repository Interface
interface DataRepository {
    suspend fun fetchData(): List<Data>
}

// Remote Repository Interface
interface RemoteDataSource {
    suspend fun getData(): List<Data>
}

// Local Repository Interface
interface LocalDataSource {
    suspend fun getData(): List<Data>
    suspend fun saveData(data: List<Data>)
}

// Remote Datasource Implementation
class RemoteDataSourceImpl : RemoteDataSource {
    override suspend fun getData(): List<Data> {
        // Implement your logic to fetch data from a remote source
        return listOf(
            Data(1, "Remote Data 1"),
            Data(2, "Remote Data 2")
        )
    }
}

// Local Datasource Implementation
class LocalDataSourceImpl : LocalDataSource {
    private val localData = mutableListOf<Data>()

    override suspend fun getData(): List<Data> {
        // Implement your logic to fetch data from local storage
        return localData
    }

    override suspend fun saveData(data: List<Data>) {
        // Implement your logic to save data to local storage
        localData.clear()
        localData.addAll(data)
    }
}


// Repository Implementation
class DataRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DataRepository {
    override suspend fun fetchData(): List<Data> {
        // Implement your logic to fetch data, either from remote or local
        val remoteData = remoteDataSource.getData()
        localDataSource.saveData(remoteData)
        return localDataSource.getData()
    }
}

// AppModule
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceImpl() // Adjust this based on your implementation
    }

    @Provides
    fun provideLocalDataSource(): LocalDataSource {
        return LocalDataSourceImpl() // Adjust this based on your implementation
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): DataRepository {
        // Implement your remote data source implementation
        return DataRepositoryImpl(remoteDataSource, localDataSource)
    }
}


// Main ViewModel
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _data = MutableStateFlow<List<Data>>(emptyList())
    val data: StateFlow<List<Data>> = _data.asStateFlow()

    init {
        viewModelScope.launch {
            val fetchedData = repository.fetchData()
            _data.value = fetchedData
        }
    }
}

// UI Main Screen Composable
@Composable
fun RepositoryPatternScreen(viewModel: MainViewModel, modifier: Modifier) {
    val data by viewModel.data.collectAsState()

    // Your UI Composable
    DataList(data, modifier)
}

// UI Data List Composable
@Composable
fun DataList(data: List<Data>, modifier: Modifier)  {
   LazyColumn(
       modifier = modifier.fillMaxSize(),
       verticalArrangement = Arrangement.SpaceEvenly,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       items(data) { item ->
           Text(text = item.content)
       }
   }
}



// Hilt Application Setup Class
@HiltAndroidApp
class HiltApplication : Application()

