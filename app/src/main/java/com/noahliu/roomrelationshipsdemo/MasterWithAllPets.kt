package com.noahliu.roomrelationshipsdemo

import androidx.room.Embedded
import androidx.room.Relation

class MasterWithAllPets (
    @Embedded var master: Master,
    @Relation (parentColumn = "id",entityColumn = "masterId") var pets:List<Pet>
        )
