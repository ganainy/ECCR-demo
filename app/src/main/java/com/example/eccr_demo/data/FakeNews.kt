package com.example.eccr_demo.data
data class FakeNews(
    val title: String,
    val content: String,
    val author: String
)


// Mock data for fake news items
val fakeNewsList = listOf(
    FakeNews("Breaking News", "Lorem ipsum dolor sit amet.", "John Doe"),
    FakeNews("Important Update", "Consectetur adipiscing elit.", "Jane Smith"),
    FakeNews("Exclusive Report", "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "David Johnson"),
    FakeNews("New Discovery", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Michael Brown"),
    FakeNews("Tech Innovation", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", "Emily Wilson"),
    FakeNews("Health Alert", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "Daniel Garcia")
)