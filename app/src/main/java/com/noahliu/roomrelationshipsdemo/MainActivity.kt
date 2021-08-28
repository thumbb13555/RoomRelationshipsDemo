package com.noahliu.roomrelationshipsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName + "My"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**開啟Stetho資料庫監視*/
        Stetho.initializeWithDefaults(this)

        Thread {
            val dao = RoomDB.getAppDatabase(applicationContext)?.dao()
            //如果列表沒有任何數據，則新增
            if (dao!!.getMasterList().isEmpty()) {
                inputMasterWithPetData()
            } else {
                //載入主人與寵物資訊
                initMaster(dao)
            }
        }.start()
        /**選擇欲查詢之主人資訊*/
        spinner_Select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Thread {
                    val dao = RoomDB.getAppDatabase(applicationContext)?.dao()
                    searchInfo(dao!!, p2)
                }.start()

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /**載入主人資訊*/
    private fun initMaster(dao: Dao) {
        val allMaster = dao.getMasterList()
        val masterArray = ArrayList<String>()
        allMaster.forEach {
            Log.d(TAG, "所有的主人名單: ${it.name}, id: ${it.id}");
            masterArray.add(it.name)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, masterArray)
        runOnUiThread {
            spinner_Select.adapter = adapter
        }
    }

    /**搜尋該主人所飼養的寵物*/
    private fun searchInfo(dao: Dao, id: Int) {
        val allMaster = dao.getMasterList()
        val list = dao.getMasterWithPets(allMaster[id].id)
        val masterInfo = "主人${list!![0].master.name} 的寶貝們為: "
        Log.d(TAG, masterInfo);
        val string = StringBuffer(masterInfo + "\n")
        val pets = list[0].pets
        pets.forEach {
            val pet = "寵物名字: ${it.petName}"
            Log.d(TAG, pet)
            string.append(pet + "\n")
        }
        runOnUiThread {
            textView_Result.text = string.toString()
        }

    }

    /**手動增加測試資料*/
    private fun inputMasterWithPetData() {
        val dao = RoomDB.getAppDatabase(applicationContext)?.dao()
        val nameList = arrayListOf("Jack", "Noah", "Sam", "Tilly", "ShiYan")
        val phoneList =
            arrayListOf("091122334455", "0936589745", "0956842398", "038569741", "0988556412")
        val petList: Array<Array<String>> = arrayOf(
            arrayOf("小黑", "小白", "小黃"),
            arrayOf("小扁", "阿飛"),
            arrayOf("貓", "狗", "兔兔", "鳥"),
            arrayOf("老王"),
            arrayOf("大哈", "二哈"),
        )

        for (i in 0 until nameList.size) {
            val uuid = UUID.randomUUID().toString()
            val master = Master(uuid, nameList[i], phoneList[i])
            dao?.insertMaster(master)
            petList[i].forEach {
                val pet = Pet(0, uuid, it)
                dao?.insertPet(pet)
            }
        }
        //載入主人資訊
        initMaster(dao!!)
    }
}
