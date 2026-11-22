package com.example.data.lectures

import com.example.data.repository.LectureCategory
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.PodcastTopicFilter
import com.example.data.repository.VerifiedIslamicChannel

/**
 * Compact factory for constructing live-verified [VerifiedIslamicChannel] entries in modular
 * scholar registry files while keeping each Kotlin file concise and readable.
 */
internal fun verifiedScholarChannel(
    key: String,
    channelId: String,
    handle: String,
    nameEn: String,
    nameUr: String = nameEn,
    nameAr: String = nameEn,
    nameHi: String = nameEn,
    specialtyEn: String,
    specialtyUr: String = specialtyEn,
    specialtyAr: String = specialtyEn,
    specialtyHi: String = specialtyEn,
    languageCode: String,
    primaryLanguageGroup: LectureLanguageGroup = LectureLanguageGroup.fromCode(languageCode),
    secondaryLanguageGroups: List<LectureLanguageGroup> = emptyList(),
    primaryCategories: List<LectureCategory>,
    podcastLanguage: PodcastLanguageSection? = null,
    podcastTopics: List<PodcastTopicFilter> = emptyList(),
    channelType: ChannelType = ChannelType.SCHOLAR,
    priority: Int = 70,
    notes: String = "Live-verified official YouTube channel and HTTP 200 Atom RSS feed."
): VerifiedIslamicChannel = VerifiedIslamicChannel(
    key = key,
    channelId = channelId,
    handle = handle,
    nameEn = nameEn,
    nameUr = nameUr,
    nameAr = nameAr,
    nameHi = nameHi,
    specialtyEn = specialtyEn,
    specialtyUr = specialtyUr,
    specialtyAr = specialtyAr,
    specialtyHi = specialtyHi,
    languageCode = languageCode,
    primaryLanguageGroup = primaryLanguageGroup,
    secondaryLanguageGroups = secondaryLanguageGroups,
    primaryCategories = primaryCategories,
    podcastLanguage = podcastLanguage,
    podcastTopics = podcastTopics,
    verificationStatus = ChannelVerificationStatus.VERIFIED,
    officialWebsite = "",
    verificationSource = "https://www.youtube.com/$handle",
    channelType = channelType,
    priority = priority,
    notes = notes
)
