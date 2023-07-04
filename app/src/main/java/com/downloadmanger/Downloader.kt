package com.downloadmanger

//return Download id which type is Long.
interface Downloader {
    fun downloadFile(url: String): Long
}