package com.example.networkconnectivity

import kotlinx.coroutines.flow.Flow

interface NetworkObserver
{
    fun observer():Flow<Status>

    enum class Status{
        Available,UnAvailable,Lost,Losing
    }

}