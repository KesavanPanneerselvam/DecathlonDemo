package com.dreamforall.decathlondemo.adapter



data class DecathlonResponseModel(
    val data: List<DataSportList> = listOf(),
    val links: Links = Links(),
    val meta: Meta = Meta()
)
    data class DataSportList(
        val attributes: Attributes = Attributes(),
        val id: Int = 0,
        val links: Links = Links(),
        val relationships: Relationships = Relationships(),
        val type: String = ""
    ) {
        data class Attributes(
            val decathlon_id: Int? = null,
            val description: String? = null,
            val icon: String? = null,
            val locale: String = "",
            val name: String? = null,
            val parent_id: Any? = null,
            val slug: String? = null,
            val weather: List<Any> = listOf()
        )
        data class Links(
            val self: String = ""
        )
        data class Relationships(
            val children: Any? = null,
            val group: Group = Group(),
            val images: Images = Images(),
            val parent: Parent = Parent(),
            val related: Any? = null,
            val tags: Tags = Tags()
        ) {
            data class Group(
                val `data`: Data = Data(),
                val links: Links = Links()
            )  {
                data class Data(
                    val id: Int = 0,
                    val type: String = ""
                )

                data class Links(
                    val self: String = ""
                )
            }

            data class Images(
                val data: List<Data> = listOf()
            ) {
                data class Data(
                    val url: String = "",
                    val variants: List<Variant> = listOf()
                ) {
                    data class Variant(
                        val medium: Medium = Medium(),
                        val thumbnail: Thumbnail = Thumbnail()
                    )  {
                        data class Medium(
                            val url: String = ""
                        )

                        data class Thumbnail(
                            val url: String = ""
                        )
                    }
                }
            }

            data class Parent(
                val `data`: Data = Data(),
                val links: Links = Links()
            )  {
                data class Data(
                    val id: Int = 0,
                    val type: String = ""
                )

                data class Links(
                    val self: String = ""
                )
            }

            data class Tags(
                val `data`: List<String> = listOf()
            )
        }
    }

    data class Links(
        val self: String = ""
    )
    data class Meta(
        val authors: List<String> = listOf(),
        val copyright: String = ""
    )