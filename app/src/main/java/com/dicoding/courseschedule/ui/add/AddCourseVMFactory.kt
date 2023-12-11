package com.dicoding.courseschedule.ui.add

import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.data.DataRepository
import android.app.Activity
import androidx.lifecycle.ViewModel
import java.lang.reflect.InvocationTargetException

class AddCourseVMFactory(private val repository: DataRepository?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(DataRepository::class.java).newInstance(repository)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }

    companion object {
        fun createFactory(activity: Activity): AddCourseVMFactory {
            val context = activity.applicationContext
                ?: throw IllegalStateException("Not yet attached to Application")

            return AddCourseVMFactory(DataRepository.getInstance(context))
        }
    }
}