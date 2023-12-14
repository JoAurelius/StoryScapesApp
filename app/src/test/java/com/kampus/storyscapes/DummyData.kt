package com.kampus.storyscapes

import com.kampus.storyscapes.model.Story

object DummyData {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story= Story(
                photoUrl = "https://example.com/photo.jpg",
                createdAt = "2023-06-30T12:34:56Z",
                name = "Dummy Story",
                description = "This is a dummy story item",
                lon = "123.456",
                id = "1234567890",
                lat = "12.34"
            )
            items.add(story)
        }
        return items
    }
}