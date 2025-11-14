data class CUEASOAnnouncement(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPosition: String = "", // President, Vice President, Secretary, etc.
    val category: String = "", // General, Academic, Events, Important
    val isPinned: Boolean = false,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class Vote(
    val id: String = "",
    val title: String = "", // e.g., "CUEASO President 2025"
    val description: String = "",
    val startDate: Long = 0,
    val endDate: Long = 0,
    val category: String = "", // President, Vice President, Secretary, etc.
    val candidates: List<Candidate> = emptyList(),
    val voterIds: List<String> = emptyList(), // Track who voted (for preventing double voting)
    val isActive: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class Candidate(
    val id: String = "",
    val name: String = "",
    val course: String = "",
    val year: String = "",
    val manifesto: String = "",
    val imageUrl: String? = null,
    val voteCount: Int = 0
)

data class Petition(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "", // Academic, Infrastructure, Welfare, etc.
    val creatorId: String = "",
    val creatorName: String = "",
    val targetSignatures: Int = 100,
    val signatoryIds: List<String> = emptyList(),
    val status: String = "Active", // Active, Under Review, Resolved, Rejected
    val responseFromCUEASO: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class StudentRepresentative(
    val id: String = "",
    val name: String = "",
    val position: String = "", // President, Vice President, etc.
    val email: String = "",
    val phone: String = "",
    val faculty: String = "",
    val imageUrl: String? = null,
    val bio: String = "",
    val officeHours: String = "",
    val officeLocation: String = ""
)
