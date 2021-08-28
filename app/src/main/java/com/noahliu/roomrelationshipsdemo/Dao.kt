package com.noahliu.roomrelationshipsdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    fun insertMaster(master: Master): Long

    @Insert
    fun insertPet(pet: Pet)

    @Query("Select * From master")
    fun getMasterList():List<Master>

    @Query("Select * From Master inner join Pet on Master.id = Pet.masterId where Master.id = :id")
    fun getMasterWithPets(id:String):List<MasterWithAllPets>?


}