package com.example.playlistmaker

enum class TrackSearchStatus {
    Success  // Успех
    ,
    NoDataFound // Не нашли
    ,
    ConnectionError // Ошибка
    ,
    ShowHistory // История
    ,
    Empty // Пусто
}