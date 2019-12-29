package com.phani.coroutinesexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    //Complete tutorial link : https://www.youtube.com/watch?v=F63mhZk-1-Y&list=PLgCYzUzKIBE_PFBRHFB_aL5stMQg3smhL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener{

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }

        }
    }

    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromApi() // wait until job is done - This statement will be on hold until this request is completed
        println("Debug: $result1")
        setTextOnMainThread(result1)


        /*
        For some reason if have a dependency with result1 and only after receiving result,
         if we want to excute another api request. then we can do this way.
         */
        val result2 = getResult2FromApi(result1) // wait until job is done
        setTextOnMainThread(result2)
    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        //withContext(Main) will switch the corountine to main
        withContext (Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult2FromApi(result : String): String {
        logThread("getResult2FromApi")
        delay(1000)
        return "Result #2"
    }


    /*
    suspend keyword means that this method can be used in background threads
    and if we want to use any methods in coroutines then those methods are to be marked as suspend
     */
    private suspend fun getResult1FromApi() : String{
        logThread("getResult1FromApi")
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}
