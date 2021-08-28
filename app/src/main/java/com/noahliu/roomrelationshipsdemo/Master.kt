package com.noahliu.roomrelationshipsdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity
data class Master(
    @PrimaryKey var id:String,
    var name:String,
    var phone:String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Master::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("masterId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Pet(
    @PrimaryKey (autoGenerate = true)
    var petId:Int,//一般數量1~10

    @ColumnInfo(name = "masterId")
    var masterId:String,//等於UUID
    var petName:String
)

