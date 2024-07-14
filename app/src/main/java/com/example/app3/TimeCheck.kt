package com.example.app3

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.app3.auth.MyListViewModel
import com.example.app3.auth.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Composable
fun RunCheckFavoritesDaily(vm: FbViewModel, listVM: MyListViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                CoroutineScope(Dispatchers.IO).launch {
                    checkFavorites(context, vm, listVM)
                }
                handler.postDelayed(this, TimeUnit.DAYS.toMillis(1))
            }
        }
        handler.post(runnable)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun checkFavorites(context: Context, vm: FbViewModel, listVM: MyListViewModel) {
    withContext(Dispatchers.IO) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        if (vm.favoriteState.value.list.isNotEmpty()) {
            for (details in vm.favoriteState.value.list) {
                if (details.countdown?.air_date?.take(10) == today) {
                    sendNotification(context, details, listVM)
                }
            }
        }
    }
}
